SRC_URI += "file://qt-kms.conf file://qt6.sh"

PACKAGE_ARCH = "${MACHINE_ARCH}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

do_install:append() {
    mkdir ${D}/${sysconfdir}
    install ${WORKDIR}/qt-kms.conf ${D}/${sysconfdir}

    mkdir ${D}/${sysconfdir}/profile.d
    install ${WORKDIR}/qt6.sh ${D}/${sysconfdir}/profile.d/qt6.sh
}

