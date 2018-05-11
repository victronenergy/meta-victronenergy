SUMMARY = "rootfs to flash an image"

SWUPDATE_INIT ?= "tiny"

IMAGE_INSTALL = " \
	base-files \
	base-passwd \
	bash \
	busybox \
	swupdate \
	u-boot-fw-utils \
	venus-version \
	${@bb.utils.contains('SWUPDATE_INIT', 'tiny', '', 'initscripts sysvinit tinylogin', d)} \
"

IMAGE_INSTALL_append_beaglebone = "e2fsprogs-mke2fs util-linux-sfdisk"
IMAGE_INSTALL_append_canvu500 = "imx-kobs mtd-utils-ubifs"
IMAGE_INSTALL_append_ccgx = "kernel-module-fbcon mtd-utils-ubifs"
IMAGE_INSTALL_append_nanopi = "e2fsprogs-mke2fs util-linux-sfdisk"

IMAGE_LINGUAS = " "

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

IMAGE_FSTYPES = "ext2.gz.u-boot"
IMAGE_ROOTFS_SIZE = "32000"

inherit image

python __anonymous() {
	d.delVarFlag('do_unpack', 'noexec')
}

addtask unpack before do_rootfs

SRC_URI = "\
	file://install.sh \
	file://swupdate \
"

add_program_instructions() {
	install -d ${IMAGE_ROOTFS}/${base_libdir}/venus
	install ${WORKDIR}/install.sh ${IMAGE_ROOTFS}/${base_libdir}/venus

	if [ "${SWUPDATE_INIT}" = "tiny" ]; then
		install ${WORKDIR}/swupdate ${IMAGE_ROOTFS}/${base_sbindir}/init
	else
		cp ${WORKDIR}/swupdate ${IMAGE_ROOTFS}${sysconfdir}/init.d
	fi
}

# remove unneeded pkg informations
ROOTFS_POSTPROCESS_COMMAND += "add_program_instructions ;"
