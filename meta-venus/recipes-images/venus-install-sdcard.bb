inherit image_types

DESCRIPTION = "Live image to program the rootfs"
INITRD_IMAGE = "venus-install-initramfs-${MACHINE}.ext2.gz.u-boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SDCARD = "${WORKDIR}/sdcard"

DEPENDS += "\
	dosfstools-native \
	mtools-native \
	parted-native \
"

DTB_beaglebone = "${KERNEL_IMAGETYPE}-bbb-venus.dtb"

SCR = "fatload-initramfs.scr"
SCR_beaglebone = "install-${MACHINE}.scr"

SWU = "venus-swu"
SWU_ccgx = "bpp3-rootfs-swu"

do_install[depends] += " \
	virtual/bootloader:do_deploy \
	virtual/kernel:do_deploy \
	venus-install-initramfs:do_rootfs \
	${SWU}:do_createlink \
"

do_install () {
	if [ -d ${SDCARD} ]; then
		rm -rf ${SDCARD}
	fi

	mkdir ${SDCARD}
	cp ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${MACHINE}.bin ${SDCARD}/${KERNEL_IMAGETYPE}
	cp ${DEPLOY_DIR_IMAGE}/MLO ${SDCARD}
	cp ${DEPLOY_DIR_IMAGE}/u-boot.img ${SDCARD}
	cp ${DEPLOY_DIR_IMAGE}/${INITRD_IMAGE} ${SDCARD}/initramfs
	cp ${DEPLOY_DIR_IMAGE}/${SCR} ${SDCARD}/boot.scr
	cp ${DEPLOY_DIR_IMAGE}/${SWU}-${MACHINE}.swu ${SDCARD}/venus.swu
	if [ -n "${DTB}" ]; then
		cp ${DEPLOY_DIR_IMAGE}/${DTB} ${SDCARD}
	fi

	zip -rj ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.sdcard.zip ${SDCARD}
	if [ -e ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.zip ]; then
		rm ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.zip
	fi
	ln -s ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.sdcard.zip ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.zip

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

        zip -j ${IMAGE}.zip ${IMAGE}
        rm ${IMAGE}
        ln -sf ${IMAGE_NAME}.img.zip ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.img.zip
}

