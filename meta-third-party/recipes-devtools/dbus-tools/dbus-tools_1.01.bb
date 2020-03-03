DESCRIPTION = "Python Dbus-tool"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS_${PN} = "python python-dbus"
SRC_URI = "git://github.com/victronenergy/dbus-tools.git;tag=v${PV}"
S = "${WORKDIR}/git"

do_install () {
    install -d ${D}${bindir}/
    install -m 0755 ${S}/dbus ${D}${bindir}/
}
