DESCRIPTION = "Boot partition image"
LICENSE = "MIT"

inherit deploy image-artifact-names nopackages

BOOT_IMAGE_SIZE = "8192"

do_deploy[depends] += "\
    dosfstools-native:do_populate_sysroot \
    mtools-native:do_populate_sysroot \
    virtual/bootloader:do_deploy \
    virtual/kernel:do_deploy \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"

do_deploy () {
    BOOT_IMAGE=${IMAGE_NAME}.vfat
    BOOTIMG=${DEPLOYDIR}/${BOOT_IMAGE}

    mkfs.vfat -S 512 -C ${BOOTIMG} ${BOOT_IMAGE_SIZE}

    for file in ${IMAGE_BOOT_FILES}; do
        mcopy -i ${BOOTIMG} -s ${DEPLOY_DIR_IMAGE}/${file} ::/
    done

    gzip ${BOOTIMG}
    ln -sf ${BOOT_IMAGE}.gz ${DEPLOYDIR}/${IMAGE_LINK_NAME}.vfat.gz
}

addtask do_deploy before do_build
