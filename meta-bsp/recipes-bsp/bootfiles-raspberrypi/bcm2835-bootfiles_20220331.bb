DESCRIPTION = "Closed source binary files to help boot the ARM on the BCM2835."
LICENSE = "Proprietary"

LIC_FILES_CHKSUM = "file://LICENCE.broadcom;md5=c403841ff2837657b2ed8e5bb474ac8d"

inherit deploy

SRC_URI = "https://github.com/raspberrypi/firmware/archive/1.${PV}.tar.gz"
SRC_URI[sha256sum] = "e186889be44a80bc148c0a020aaf580633ddb10ee314c1c7299c74fd73db3dd1"

COMPATIBLE_MACHINE = "raspberrypi"

S = "${WORKDIR}/firmware-1.${PV}/boot"

do_deploy() {
    install -d ${DEPLOYDIR}/${PN}
    cp ${S}/*.elf ${S}/*.dat ${S}/*.bin ${S}/LICENCE.broadcom ${DEPLOYDIR}/${PN}
}

addtask deploy before do_package after do_install
do_deploy[dirs] += "${DEPLOYDIR}/${PN}"

PACKAGE_ARCH = "${MACHINE_ARCH}"
