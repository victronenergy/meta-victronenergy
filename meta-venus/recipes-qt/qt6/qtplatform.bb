SRC_URI += "file://qt-kms.conf file://qt6.sh"

PACKAGE_ARCH = "${MACHINE_ARCH}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

do_install:append() {
    mkdir ${D}/${sysconfdir}
    install ${UNPACKDIR}/qt-kms.conf ${D}/${sysconfdir}

    mkdir ${D}/${sysconfdir}/profile.d
    install ${UNPACKDIR}/qt6.sh ${D}/${sysconfdir}/profile.d/qt6.sh

    # GUIv2 is intentionally launched as root on Venus OS. Chromium refuses
    # to start its sandbox as root, so QtWebEngine must use its supported
    # embedded/root mode regardless of which start-gui selector launches it.
    echo 'export QTWEBENGINE_DISABLE_SANDBOX=1' >> \
        ${D}/${sysconfdir}/profile.d/qt6.sh
}
