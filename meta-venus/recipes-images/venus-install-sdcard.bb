inherit image_types

DESCRIPTION = "Live image to program the rootfs"
INITRD_IMAGE = "venus-install-initramfs-${MACHINE}.ext2.gz.u-boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SDCARD = "${WORKDIR}/sdcard"

do_install[depends] += " \
	virtual/bootloader:do_deploy \
	virtual/kernel:do_deploy \
	venus-install-initramfs:do_rootfs \
	venus-swu:do_install \
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
	cp ${DEPLOY_DIR_IMAGE}/fatload-initramfs.scr ${SDCARD}/boot.scr
	cp ${DEPLOY_DIR_IMAGE}/venus-swu-${MACHINE}.swu ${SDCARD}/venus.swu

	zip -rj ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.sdcard.zip ${SDCARD}
	if [ -e ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.zip ]; then
		rm ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.zip
	fi
	ln -s ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.sdcard.zip ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.zip
}

