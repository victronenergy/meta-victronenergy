inherit image_types

IMAGE_TYPES += "live.img.gz"

IMAGE_TYPEDEP_live.img = "ext3"

IMAGE_DEPENDS_live.img = "\
	dosfstools-native:do_populate_sysroot \
	mtools-native:do_populate_sysroot \
	parted-native:do_populate_sysroot \
	virtual/bootloader \
	virtual/kernel:do_deploy \
	"

IMAGE_CMD_live.img () {
	BOOTIMG=${WORKDIR}/boot.img
	LIVE_IMAGE=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.live.img

	# create live image with two partition, first to boot, second the rootfs itself
	dd if=/dev/zero of=${LIVE_IMAGE} count=1024 bs=1M
	parted ${LIVE_IMAGE} mktable msdos
	parted ${LIVE_IMAGE} mkpart p fat32 1 100
	parted ${LIVE_IMAGE} mkpart p ext3 100 1024

	# format a seperate partion as FAT
	if [ -f ${BOOTIMG} ]; then rm ${BOOTIMG}; fi
	BOOT_BLOCKS=$(parted -s ${LIVE_IMAGE} unit b print | awk '/ 1 / { print substr($4, 1, length($4 -1)) / 1024 }')
	mkfs.vfat -n BOOT -S 512 -C ${BOOTIMG} $BOOT_BLOCKS

	# and store the files need on it
	mcopy -i ${BOOTIMG} -s ${DEPLOY_DIR_IMAGE}/uImage ::/uImage
	mcopy -i ${BOOTIMG} -s ${DEPLOY_DIR_IMAGE}/live.scr ::/boot.scr

	# copy partitions into the image
	dd if=${BOOTIMG} of=${LIVE_IMAGE} conv=notrunc seek=1 bs=1M
	dd if=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ext3 of=${LIVE_IMAGE} bs=512 seek=194560
}

