SUMMARY = "MACHINE specific qt4 env settings"
DESCRIPTION = " \
The goal of this is that qt4 applications run from the command line \
by default, without requiring all kinds of variables to be set manually. \
It is a seperate recipe to prevent qt4-embedded becoming MACHINE dependend. \
"

SRC_URI += "file://qt4.sh"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

do_install:append() {
    mkdir -p ${D}/${sysconfdir}/profile.d
    install ${UNPACKDIR}/qt4.sh ${D}/${sysconfdir}/profile.d
}
