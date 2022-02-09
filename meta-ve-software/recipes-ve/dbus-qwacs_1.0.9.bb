DESCRIPTION = "QT console application to present Quby wireless AC sensors on the DBus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit qmakeve
inherit daemontools

RDEPENDS:${PN} = "dbus"
PR = "r0"
SRC_URI = " \
    gitsm://github.com/victronenergy/dbus_qwacs.git;branch=master;protocol=https;tag=v${PV} \
"

S = "${WORKDIR}/git"

DEST_DIR = "${D}${BASE_DIR}"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/${PN}"
