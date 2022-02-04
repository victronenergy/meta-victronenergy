DESCRIPTION = "Live image to program the rootfs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy image-artifact-names nopackages

INITRD_IMAGE = "venus-install-initramfs-${MACHINE}.cpio.gz.u-boot"
SCR = "install.scr"

BOARD_IDS ?= ""

IMAGE_NAME = "${IMAGE_BASENAME}-${MACHINE}-${DATETIME}-${DISTRO_VERSION}"
IMAGE_NAME[vardepsexclude] += "DATETIME"

python () {
    dtb = d.getVar('KERNEL_DEVICETREE') or ''
    d.setVar('DTB', ' '.join(map(os.path.basename, dtb.split())))
}

INSTALL_FILES = "\
    ${SPL_BINARY} \
    ${UBOOT_IMAGE} \
    ${SCR}:boot.scr \
    ${KERNEL_IMAGETYPE}-${MACHINE}.bin:${KERNEL_IMAGETYPE} \
    ${DTB} \
    ${INITRD_IMAGE}:initramfs \
    ${SWU}-${MACHINE}.swu:venus.swu \
"

do_deploy[depends] += " \
    virtual/bootloader:do_deploy \
    virtual/kernel:do_deploy \
    venus-install-initramfs:do_image_complete \
    ${SWU}:do_swuimage \
    dosfstools-native:do_populate_sysroot \
    mtools-native:do_populate_sysroot \
    parted-native:do_populate_sysroot \
    zip-native:do_populate_sysroot \
"

SDCARD = "${WORKDIR}/sdcard"

do_prepare_sdcard() {
    rm -rf ${SDCARD}
    mkdir ${SDCARD}
}

addtask do_prepare_sdcard before do_deploy

python do_board_ids() {
    import quopri

    sdcard = d.getVar('SDCARD')
    board_ids = d.getVar('BOARD_IDS').split()

    for b in board_ids:
        bn, bi = b.split(':')
        bi = quopri.decodestring(bi)

        with open(os.path.join(sdcard, 'board_id_' + bn), 'wb') as f:
            f.write(bi)
}

addtask do_board_ids after do_prepare_sdcard before do_deploy

do_deploy () {
    for file in ${INSTALL_FILES}; do
        src=${file%:*}
        dst=${file#*:}
        cp ${DEPLOY_DIR_IMAGE}/${src} ${SDCARD}/${dst}
    done

    zip -rj ${DEPLOYDIR}/${IMAGE_NAME}.sdcard.zip ${SDCARD}
    ln -sf ${IMAGE_NAME}.sdcard.zip ${DEPLOYDIR}/${IMAGE_LINK_NAME}.sdcard.zip

    # size of card contents in MB
    SIZE=$(du -sm ${SDCARD} | sed 's/[^0-9].*//')

    # create vfat image
    FSIMAGE=${WORKDIR}/image.vfat
    FSSIZE=$(expr ${SIZE} + 4)
    rm -f ${FSIMAGE}
    mkfs.vfat -S 512 -C ${FSIMAGE} $(expr ${FSSIZE} \* 1024)
    mcopy -i ${FSIMAGE} ${SDCARD}/* ::/

    # create partitioned image
    IMAGE=${DEPLOYDIR}/${IMAGE_NAME}.img
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
    ln -sf ${IMAGE_NAME}.img.zip ${DEPLOYDIR}/${IMAGE_LINK_NAME}.img.zip
}

addtask do_deploy after do_install

