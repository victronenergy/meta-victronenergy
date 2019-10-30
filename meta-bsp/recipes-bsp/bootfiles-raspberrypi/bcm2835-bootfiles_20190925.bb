DESCRIPTION = "Closed source binary files to help boot the ARM on the BCM2835."
LICENSE = "Proprietary"

LIC_FILES_CHKSUM = "file://LICENCE.broadcom;md5=4a4d169737c0786fb9482bb6d30401d1"

inherit deploy

SRC_URI = "https://github.com/raspberrypi/firmware/archive/1.${PV}.tar.gz"
SRC_URI[md5sum] = "8dfd4cc3c3ac084760c4b7f8c660b6d4"
SRC_URI[sha256sum] = "b3c5c9d3cda1f77caf317b8d1f0496cd7ca791ddaeec8207a5a1940111483509"

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
