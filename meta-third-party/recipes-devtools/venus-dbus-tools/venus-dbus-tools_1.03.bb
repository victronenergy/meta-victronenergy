DESCRIPTION = "Python Dbus-tool"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit python-compile

RDEPENDS:${PN} = "python3-core python3-dbus python3-lxml python3-pprint"
UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "git://github.com/victronenergy/dbus-tools.git;branch=master;protocol=https"
SRCREV = "53b18b055f101c74f9beb13ed2199b95e644ada1"
S = "${WORKDIR}/git"

do_install () {
    install -d ${D}${bindir}/
    install -m 0755 ${S}/dbus ${D}${bindir}/
}
