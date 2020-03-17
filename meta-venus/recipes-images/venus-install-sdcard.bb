inherit image_types uboot-config

DESCRIPTION = "Live image to program the rootfs"
INITRD_IMAGE = "venus-install-initramfs-${MACHINE}.ext2.gz.u-boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SDCARD = "${WORKDIR}/sdcard"
PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "\
    dosfstools-native \
    mtools-native \
    parted-native \
    zip-native \
"

DTB = "${KERNEL_DEVICETREE}"

SCR = "install-${MACHINE}.scr"

SRC_URI_beaglebone += "file://board_id_octogx"
SRC_URI_einstein += "\
    file://board_id_cerbogx \
"
SRC_URI_nanopi += "\
    file://board_id_easysolar \
    file://board_id_easysolar_a9 \
    file://board_id_easysolar_a10 \
    file://board_id_maxigx \
    file://board_id_maxigx_a10 \
    file://board_id_multiplus2 \
    file://board_id_multiplus2_a10 \
    file://board_id_paygo \
"

IMAGE_NAME = "${IMAGE_BASENAME}-${MACHINE}-${DATETIME}-${DISTRO_VERSION}"
IMAGE_NAME[vardepsexclude] += "DATETIME"

INSTALL_FILES = "\
    ${SPL_BINARY} \
    ${UBOOT_BINARY} \
    ${SCR}:boot.scr \
    ${KERNEL_IMAGETYPE}-${MACHINE}.bin:${KERNEL_IMAGETYPE} \
    ${DTB} \
    ${INITRD_IMAGE}:initramfs \
    ${SWU}-${MACHINE}.swu:venus.swu \
"

do_install[depends] += " \
    virtual/bootloader:do_deploy \
    virtual/kernel:do_deploy \
    venus-install-initramfs:do_image_complete \
    ${SWU}:do_swuimage \
"

do_install () {
    if [ -d ${SDCARD} ]; then
        rm -rf ${SDCARD}
    fi

    mkdir ${SDCARD}

    for file in ${INSTALL_FILES}; do
        src=${file%:*}
        dst=${file#*:}
        cp ${DEPLOY_DIR_IMAGE}/${src} ${SDCARD}/${dst}
    done

    find ${WORKDIR} -name "board_id_*" -exec cp {} ${SDCARD} \;

    zip -rj ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.sdcard.zip ${SDCARD}
    ln -sf ${IMAGE_NAME}.sdcard.zip ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.sdcard.zip

    # size of card contents in MB
    SIZE=$(du -sm ${SDCARD} | sed 's/[^0-9].*//')

    # create vfat image
    FSIMAGE=${WORKDIR}/image.vfat
    FSSIZE=$(expr ${SIZE} + 4)
    rm -f ${FSIMAGE}
    mkfs.vfat -S 512 -C ${FSIMAGE} $(expr ${FSSIZE} \* 1024)
    mcopy -i ${FSIMAGE} ${SDCARD}/* ::/

    # create partitioned image
    IMAGE=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.img
    IMAGE_SIZE=$(expr ${FSSIZE} + 1)
    dd if=/dev/null of=${IMAGE} bs=1M seek=${IMAGE_SIZE}
    parted ${IMAGE} -- \
        mklabel msdos \
        mkpart p fat32 1MiB -1s \
        set 1 boot on

    # copy vfat image into partition
    dd if=${FSIMAGE} of=${IMAGE} bs=1M seek=1

    # write boot loader if required
    if [ -n "${SDIMAGE_BOOT_FILE}" ]; then
        dd if=${DEPLOY_DIR_IMAGE}/${SDIMAGE_BOOT_FILE} of=${IMAGE} conv=notrunc bs=1k seek=${SDIMAGE_BOOT_FILE_OFFS}
    fi

    zip -j ${IMAGE}.zip ${IMAGE}
    rm ${IMAGE}
    ln -sf ${IMAGE_NAME}.img.zip ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.img.zip
}

