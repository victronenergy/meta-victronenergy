inherit image_types

SUMMARY = "Live image to upgrade the rootfs."
DESCRIPTION = " \
	An upgrade image differs from the install (production) image that it will \
	not completely reflash the device. Settings / logs etc are kept. Furthermore \
	it should survive power cuts / card removal etc (or at least be able to resume). \
	\
	On a color control it should not modify the bootloader (at least not with \
	extreme care) since booting the upgrade image depends on the bootloader in NAND! \
"
INITRD_IMAGE = "venus-upgrade-initramfs-${MACHINE}.ext2.gz.u-boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SDCARD = "${WORKDIR}/sdcard"

SWU = "venus-swu"
SWU_ccgx = "bpp3-rootfs-swu"

do_install[depends] += " \
	virtual/bootloader:do_deploy \
	virtual/kernel:do_deploy \
	venus-upgrade-initramfs:do_rootfs \
	${SWU}:do_createlink \
"

do_install () {
	if [ -d ${SDCARD} ]; then
		rm -rf ${SDCARD}
	fi

	mkdir ${SDCARD}
	cp ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${MACHINE}.bin ${SDCARD}/${KERNEL_IMAGETYPE}
	cp ${DEPLOY_DIR_IMAGE}/${INITRD_IMAGE} ${SDCARD}/initramfs
	cp ${DEPLOY_DIR_IMAGE}/upgrade.scr ${SDCARD}/boot.scr
	cp ${DEPLOY_DIR_IMAGE}/${SWU}-${MACHINE}.swu ${SDCARD}/venus.swu

	zip -rj ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.sdcard.zip ${SDCARD}
	if [ -e ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.zip ]; then
		rm ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.zip
	fi
	ln -s ${IMAGE_NAME}.sdcard.zip ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.zip
}

