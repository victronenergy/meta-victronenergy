SUMMARY = "rootfs to flash an image"

SWUPDATE_INIT ?= "tiny"

IMAGE_INSTALL = " \
	base-files \
	base-passwd \
	busybox \
	busybox-mdev \
	curl \
	swupdate \
	u-boot-env-tools \
	venus-version \
	${@bb.utils.contains('SWUPDATE_INIT', 'tiny', '', 'initscripts sysvinit tinylogin', d)} \
"

IMAGE_INSTALL_append_beaglebone = "e2fsprogs-mke2fs util-linux-sfdisk"
IMAGE_INSTALL_append_ccgx = "kernel-module-fbcon mtd-utils-ubifs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR="r1"

IMAGE_CLASSES += " image_types_uboot"
IMAGE_FSTYPES = "ext2.gz.u-boot"
IMAGE_DEVICE_TABLES = "conf/machine/${MACHINE}/device_table.txt"
IMAGE_ROOTFS_SIZE = "32000"

inherit image

python __anonymous() {
	d.delVarFlag('do_unpack', 'noexec')
}

addtask unpack before do_rootfs

SRC_URI = "file://swupdate"

remove_locale_data_files() {
	printf "Post processing local %s\n" ${IMAGE_ROOTFS}${libdir}/locale
	rm -rf ${IMAGE_ROOTFS}${libdir}/locale
}

add_program_instructions() {
	if [ "${SWUPDATE_INIT}" = "tiny" ]; then
		install ${WORKDIR}/swupdate ${IMAGE_ROOTFS}/${base_sbindir}/init
	else
		cp ${WORKDIR}/swupdate ${IMAGE_ROOTFS}${sysconfdir}/init.d
	fi
}

# remove unneeded pkg informations
ROOTFS_POSTPROCESS_COMMAND += "remove_packaging_data_files ; add_program_instructions ;"
ROOTFS_POSTPROCESS_COMMAND += "remove_locale_data_files ; "
