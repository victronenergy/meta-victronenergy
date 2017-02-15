DESCRIPTION = "Closed source binary files to help boot the ARM on the BCM2835."
LICENSE = "Proprietary"

LIC_FILES_CHKSUM = "file://LICENCE.broadcom;md5=4a4d169737c0786fb9482bb6d30401d1"

inherit deploy

RPIFW_SRCREV ?= "390f53ed0fd79df274bdcc81d99e09fa262f03ab"
RPIFW_DATE ?= "20160622"
RPIFW_SRC_URI ?= "git://github.com/raspberrypi/firmware.git;protocol=git;branch=master"
RPIFW_S ?= "${WORKDIR}/git"

SRC_URI = "${RPIFW_SRC_URI}"
SRCREV = "${RPIFW_SRCREV}"
PV = "${RPIFW_DATE}"

COMPATIBLE_MACHINE = "raspberrypi"

S = "${RPIFW_S}/boot"

PR = "r3"

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
