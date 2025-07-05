DESCRIPTION = "Boot partition image"
LICENSE = "MIT"

inherit deploy image-artifact-names nopackages

PACKAGE_ARCH = "${MACHINE_ARCH}"

BOOT_IMAGE_SIZE = "8192"
BOOT_IMAGE_SIZE:rpi = "90000"

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
    files="${IMAGE_BOOT_FILES}"
    for file in $files; do
        src=${file%%;*}
        dst=${file#*;}

        if [ "$dst" = "" ]; then
            dst="$src"
        fi

        tgdir="$(dirname ${WORKDIR}/bootfiles/$dst)"
        mkdir -p "$tgdir"
        cp -rL ${DEPLOY_DIR_IMAGE}/${src} $tgdir
    done

    BOOT_IMAGE_FILE=${IMAGE_NAME}.vfat
    BOOTIMG=${DEPLOYDIR}/${BOOT_IMAGE_FILE}

    mkfs.vfat -S 512 -C ${BOOTIMG} ${BOOT_IMAGE_SIZE}
    mcopy -i ${BOOTIMG} -s ${WORKDIR}/bootfiles/* ::/

    gzip ${BOOTIMG}
    ln -sf ${BOOT_IMAGE_FILE}.gz ${DEPLOYDIR}/${IMAGE_LINK_NAME}.vfat.gz
}
do_deploy[cleandirs] += "${WORKDIR}/bootfiles"

addtask do_deploy before do_build
