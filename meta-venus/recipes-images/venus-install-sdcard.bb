inherit image_types

DESCRIPTION = "Live image to program the rootfs"
INITRD_IMAGE = "venus-install-initramfs-${MACHINE}.ext2.gz.u-boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SDCARD = "${WORKDIR}/sdcard"

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
}

