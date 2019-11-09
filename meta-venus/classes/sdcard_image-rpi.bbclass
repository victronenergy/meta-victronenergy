inherit image_types

#
# Create an image that can by written onto a SD card using dd.
#
# The disk layout used is:
#
#    0                      -> IMAGE_ROOTFS_ALIGNMENT         - reserved for other data
#    IMAGE_ROOTFS_ALIGNMENT -> BOOT_SPACE                     - bootloader and kernel
#    BOOT_SPACE             -> ROOTFS_SIZE                    - rootfs
#    ROOTFS_SIZE            -> SDIMG_SIZE                     - data
#

# Default Free space = 1.3x
# Use IMAGE_OVERHEAD_FACTOR to add more space

#            4MiB              40MiB      SDIMG_ROOTFS  SDIMG_ROOTFS  SDIMG_DATA
# <-----------------------> <----------> <-----------> <-----------> <---------->
#  ------------------------ ------------ ------------- ------------- ------------
# | IMAGE_ROOTFS_ALIGNMENT | BOOT_SPACE | ROOTFS_SIZE | ROOTFS_SIZE | DATA_SPACE |
#  ------------------------ ------------ ------------- ------------- ------------
# ^                        ^            ^             ^             ^
# |                        |            |             |             |
# 0                      4MiB     4MiB + 40MiB     44Mib +       44Mib +
#                                               SDIMG_ROOTFS  2*SDIMG_ROOTFS

# This image depends on the rootfs image
IMAGE_TYPEDEP_rpi-sdimg = "${SDIMG_ROOTFS_TYPE}"

# Boot partition size [in KiB] (will be rounded up to IMAGE_ROOTFS_ALIGNMENT)
BOOT_SPACE ?= "40960"

# Data partition size [in KiB]
DATA_SPACE ?= "786432"

# Set alignment to 4MB [in KiB]
IMAGE_ROOTFS_ALIGNMENT = "4096"

# Use an uncompressed ext4 by default as rootfs
SDIMG_ROOTFS_TYPE ?= "ext4.gz"
SDIMG_ROOTFS = "${IMGDEPLOYDIR}/${IMAGE_NAME}.rootfs.${SDIMG_ROOTFS_TYPE}"

do_image_rpi_sdimg[depends] = " \
    parted-native:do_populate_sysroot \
    mtools-native:do_populate_sysroot \
    dosfstools-native:do_populate_sysroot \
    zip-native:do_populate_sysroot \
    virtual/kernel:do_deploy \
    venus-boot-image:do_rootfs \
    bcm2835-bootfiles:do_deploy \
"

# SD card image name
SDIMG = "${IMGDEPLOYDIR}/${IMAGE_NAME}.rootfs.rpi-sdimg"

# Additional files and/or directories to be copied into the vfat partition from the IMAGE_ROOTFS.
FATPAYLOAD ?= ""

IMAGE_CMD_rpi-sdimg () {

    # Align partitions
    BOOT_SPACE_ALIGNED=$(expr ${BOOT_SPACE} + ${IMAGE_ROOTFS_ALIGNMENT} - 1)
    BOOT_SPACE_ALIGNED=$(expr ${BOOT_SPACE_ALIGNED} - ${BOOT_SPACE_ALIGNED} % ${IMAGE_ROOTFS_ALIGNMENT})

    ROOT_SPACE_ALIGNED=$(expr ${ROOTFS_SIZE} + ${IMAGE_ROOTFS_ALIGNMENT} - 1)
    ROOT_SPACE_ALIGNED=$(expr ${ROOT_SPACE_ALIGNED} - ${ROOT_SPACE_ALIGNED} % ${IMAGE_ROOTFS_ALIGNMENT})

    SDIMG_SIZE=$(expr ${IMAGE_ROOTFS_ALIGNMENT} + ${BOOT_SPACE_ALIGNED} + ${ROOT_SPACE_ALIGNED} + ${ROOT_SPACE_ALIGNED} + ${DATA_SPACE})

    echo "Creating filesystem with Boot partition ${BOOT_SPACE_ALIGNED} KiB and RootFS $ROOTFS_SIZE KiB"

    # Initialize sdcard image file
    dd if=/dev/zero of=${SDIMG} bs=1024 count=0 seek=${SDIMG_SIZE}

    # Create partition table
    parted -s ${SDIMG} mklabel msdos
    # Create boot partition and mark it as bootable
    parted -s ${SDIMG} unit KiB mkpart primary fat32 ${IMAGE_ROOTFS_ALIGNMENT} $(expr ${BOOT_SPACE_ALIGNED} \+ ${IMAGE_ROOTFS_ALIGNMENT})
    parted -s ${SDIMG} set 1 boot on
    # Create rootfs partition
    parted -s ${SDIMG} -- unit KiB mkpart primary ext2 $(expr ${BOOT_SPACE_ALIGNED} \+ ${IMAGE_ROOTFS_ALIGNMENT}) $(expr ${BOOT_SPACE_ALIGNED} \+ ${IMAGE_ROOTFS_ALIGNMENT} \+ ${ROOT_SPACE_ALIGNED})
    # Create second rootfs partition
    END_ROOT2=$(expr ${BOOT_SPACE_ALIGNED} \+ ${IMAGE_ROOTFS_ALIGNMENT} \+ ${ROOT_SPACE_ALIGNED} \+ ${ROOT_SPACE_ALIGNED})
    parted -s ${SDIMG} -- unit KiB mkpart primary ext2 $(expr ${BOOT_SPACE_ALIGNED} \+ ${IMAGE_ROOTFS_ALIGNMENT} \+ ${ROOT_SPACE_ALIGNED}) ${END_ROOT2}
    # Create data partition to the end of disk
    parted -s ${SDIMG} -- unit KiB mkpart primary ext2 ${END_ROOT2} -1s

    parted ${SDIMG} print

    # Create venus informational tree for data partition
    install -d ${WORKDIR}/data/venus
    cp ${IMAGE_ROOTFS}/opt/victronenergy/version ${WORKDIR}/data/venus/image-version

    # Create empty data partition with only informational venus tree
    DATA_BLOCKS=$(LC_ALL=C parted -s ${SDIMG} unit b print | awk '/ 4 / { print substr($4, 1, length($4 -1)) / 512 /2 }')
    rm -f ${WORKDIR}/data.img
    dd if=/dev/zero of=${WORKDIR}/data.img bs=512 count=0 seek=${DATA_BLOCKS}
    mkfs.ext4 -F ${WORKDIR}/data.img -d ${WORKDIR}/data

    # Burn Partitions
    zcat ${DEPLOY_DIR_IMAGE}/venus-boot-image-raspberrypi2.vfat.gz | dd of=${SDIMG} conv=notrunc seek=1 bs=$(expr ${IMAGE_ROOTFS_ALIGNMENT} \* 1024)
    zcat ${SDIMG_ROOTFS} | dd of=${SDIMG} conv=notrunc seek=1 bs=$(expr 1024 \* ${BOOT_SPACE_ALIGNED} + ${IMAGE_ROOTFS_ALIGNMENT} \* 1024)
    dd if=${WORKDIR}/data.img of=${SDIMG} conv=notrunc seek=1 bs=$(expr 1024 \* ${BOOT_SPACE_ALIGNED} + ${IMAGE_ROOTFS_ALIGNMENT} \* 1024 + ${ROOT_SPACE_ALIGNED} \* 2048)
}

ROOTFS_POSTPROCESS_COMMAND += " rpi_generate_sysctl_config ; "

rpi_generate_sysctl_config() {
    # systemd sysctl config
    test -d ${IMAGE_ROOTFS}${sysconfdir}/sysctl.d && \
        echo "vm.min_free_kbytes = 8192" > ${IMAGE_ROOTFS}${sysconfdir}/sysctl.d/rpi-vm.conf

    # sysv sysctl config
    IMAGE_SYSCTL_CONF="${IMAGE_ROOTFS}${sysconfdir}/sysctl.conf"
    test -e ${IMAGE_ROOTFS}${sysconfdir}/sysctl.conf && \
        sed -e "/vm.min_free_kbytes/d" -i ${IMAGE_SYSCTL_CONF}
    echo "" >> ${IMAGE_SYSCTL_CONF} && echo "vm.min_free_kbytes = 8192" >> ${IMAGE_SYSCTL_CONF}
}
