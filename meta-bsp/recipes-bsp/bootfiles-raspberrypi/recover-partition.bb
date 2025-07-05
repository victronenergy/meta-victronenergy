LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "raspberrypi"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy

RESCUE_IMAGE_SIZE = "90000"
B = "${WORKDIR}/build"

do_compile() {
    echo "boot_partition=2" >> ${B}/autoboot.txt
}

do_deploy () {
    RESCUE_IMG=${DEPLOYDIR}/${PN}.vfat

    mkfs.vfat -S 512 -C ${RESCUE_IMG} ${RESCUE_IMAGE_SIZE}
    mcopy -i ${RESCUE_IMG} -s ${B}/* ::/

    mcopy -i ${RESCUE_IMG} -s ${DEPLOY_DIR_IMAGE}/bcm2835-bootfiles/*  ::/
}

do_deploy[depends] += " \
    bcm2835-bootfiles:do_deploy \
    dosfstools-native:do_populate_sysroot \
    mtools-native:do_populate_sysroot \
"

addtask deploy before do_package after do_install

