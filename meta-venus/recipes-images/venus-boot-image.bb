DESCRIPTION = "Boot partition image"
LICENSE = "MIT"

BOOT_IMAGE_SIZE = "8192"
BOOT_IMAGE_SIZE_raspberrypi2 = "40960"

do_rootfs[depends] += "\
	dosfstools-native:do_populate_sysroot \
	mtools-native:do_populate_sysroot \
	virtual/bootloader:do_deploy \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
do_package[noexec] = "1"
do_package_qa[noexec] = "1"
do_packagedata[noexec] = "1"
do_package_write_ipk[noexec] = "1"
do_package_write_deb[noexec] = "1"
do_package_write_rpm[noexec] = "1"

do_rootfs () {
    BOOT_IMAGE=${IMAGE_NAME}.vfat
    BOOTIMG=${DEPLOY_DIR_IMAGE}/${BOOT_IMAGE}

    rm -f ${BOOTIMG}
    mkfs.vfat -S 512 -C ${BOOTIMG} ${BOOT_IMAGE_SIZE}

    for file in ${IMAGE_BOOT_FILES}; do
        mcopy -i ${BOOTIMG} -s ${DEPLOY_DIR_IMAGE}/${file} ::/
    done

    gzip ${BOOTIMG}
    ln -sf ${BOOT_IMAGE}.gz ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.vfat.gz
}

addtask do_rootfs before do_build
