DESCRIPTION = "Closed source binary files to help boot the ARM on the BCM2835."
LICENSE = "Proprietary"

LIC_FILES_CHKSUM = "file://LICENCE.broadcom;md5=4a4d169737c0786fb9482bb6d30401d1"

inherit deploy

SRC_URI = "https://github.com/raspberrypi/firmware/archive/1.${PV}.tar.gz"
SRC_URI[md5sum] = "4d27c1888a7bab3097471906e7b4a319"
SRC_URI[sha256sum] = "46ce28c8d87ef22bdcc57ac1836ca3f04d1ec6f46580ff5a30bf76b3c0822117"

COMPATIBLE_MACHINE = "raspberrypi"

S = "${WORKDIR}/firmware-1.${PV}/boot"

do_deploy() {
    install -d ${DEPLOYDIR}/${PN}
    cp ${S}/*.elf ${S}/*.dat ${S}/*.bin ${S}/LICENCE.broadcom ${DEPLOYDIR}/${PN}

    # Symlinks to make boot image construction easier
    install -d ${DEPLOYDIR}/boot
    for i in ${DEPLOYDIR}/${PN}/*; do
        fn=`basename $i`
        ln -sf ../${PN}/$fn ${DEPLOYDIR}/boot/$fn
    done
}

addtask deploy before do_package after do_install
do_deploy[dirs] += "${DEPLOYDIR}/${PN} ${DEPLOYDIR}/boot"

PACKAGE_ARCH = "${MACHINE_ARCH}"
