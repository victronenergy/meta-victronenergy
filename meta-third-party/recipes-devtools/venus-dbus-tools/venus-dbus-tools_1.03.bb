DESCRIPTION = "Python Dbus-tool"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit python-compile

RDEPENDS:${PN} = "python3-core python3-dbus python3-lxml python3-pprint"
SRC_URI = "git://github.com/victronenergy/dbus-tools.git;branch=master;protocol=https;tag=v${PV}"
S = "${WORKDIR}/git"

do_install () {
    install -d ${D}${bindir}/
    install -m 0755 ${S}/dbus ${D}${bindir}/
}
