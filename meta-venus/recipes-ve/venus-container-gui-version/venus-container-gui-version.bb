SUMMARY = "Forces Settings/Gui/RunningVersion=2 in the container, so nginx serves gui-v2 instead of gui-v1"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://set-container-gui-version.sh"

S = "${S_UNUSED}"

inherit daemontools

DAEMONTOOLS_RUN = "${bindir}/set-container-gui-version.sh"

RDEPENDS:${PN} += "dbus"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/set-container-gui-version.sh ${D}${bindir}/
}
