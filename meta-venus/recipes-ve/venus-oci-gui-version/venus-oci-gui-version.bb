SUMMARY = "Selects the browser-hosted GUI in OCI images"
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
