DESCRIPTION = "Closed source binary files to help boot the ARM on the BCM2835."
LICENSE = "Proprietary"

LIC_FILES_CHKSUM = "file://LICENCE.broadcom;md5=4a4d169737c0786fb9482bb6d30401d1"

inherit deploy

SRC_URI = "https://github.com/artynet/firmware/archive/1.${PV}.tar.gz"
SRC_URI[md5sum] = "92d1019662471c0fa4ec8433fdd08620"
SRC_URI[sha256sum] = "7a9314362d67b44f8e36a68ce35d91493f56f6b386a4855086a1779ac00c7602"

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
