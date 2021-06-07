SUMMARY = "MACHINE specific qt4 env settings"
DESCRIPTION = " \
The goal of this is that qt4 applications run from the command line \
by default, without requiring all kinds of variables to be set manually. \
It is a seperate recipe to prevent qt4-embedded becoming MACHINE dependend. \
"

SRC_URI += "file://qt4.sh"

PACKAGE_ARCH = "${MACHINE_ARCH}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

do_install_append() {
    mkdir -p ${D}/${sysconfdir}/profile.d
    install ${WORKDIR}/qt4.sh ${D}/${sysconfdir}/profile.d
}
