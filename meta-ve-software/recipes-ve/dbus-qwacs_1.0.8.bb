DESCRIPTION = "QT console application to present Quby wireless AC sensors on the DBus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit qmakeve
inherit daemontools

RDEPENDS_${PN} = "dbus"
PR = "r0"
SRC_URI = " \
    gitsm://github.com/victronenergy/dbus_qwacs.git;protocol=https;tag=v${PV} \
"

S = "${WORKDIR}/git"

DEST_DIR = "${D}${BASE_DIR}"
DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/${PN}"
