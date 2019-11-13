DESCRIPTION = "Closed source binary files to help boot the ARM on the BCM2835."
LICENSE = "Proprietary"

LIC_FILES_CHKSUM = "file://LICENCE.broadcom;md5=4a4d169737c0786fb9482bb6d30401d1"

inherit deploy

SRC_URI = "https://github.com/raspberrypi/firmware/archive/1.${PV}.tar.gz"
SRC_URI[md5sum] = "f8c50e6c7ab92b65016b7701ad42172b"
SRC_URI[sha256sum] = "e50f57b88ce09edb09a0a12b16173fced46eeae4e6e948b587af81ce745cf1fc"

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
