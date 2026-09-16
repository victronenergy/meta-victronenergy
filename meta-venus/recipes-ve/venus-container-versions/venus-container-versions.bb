SUMMARY = "Populates /var/run/versions in the container, so venus-platform reports a firmware version to the GUI"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://container-versions.sh"

S = "${S_UNUSED}"

inherit update-rc.d

INITSCRIPT_NAME = "container-versions.sh"
INITSCRIPT_PARAMS = "start 92 S ."

RDEPENDS:${PN} += "venus-version"

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/container-versions.sh ${D}${sysconfdir}/init.d/
}
