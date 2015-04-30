DESCRIPTION = "DBus to modbus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit qt4e
inherit ve_package
inherit daemontools

PR = "r20"
SRC_URI = "git://github.com/victronenergy/dbus_modbustcp.git;tag=v${PV};protocol=https"
S = "${WORKDIR}/git"
DEST_DIR = "${D}${bindir}"
DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 2000000 -s 1000000 -a 100000000 ${bindir}/${PN}"
DAEMONTOOLS_DOWN = "1"

# why?
EXTRA_QMAKEVARS_POST += "DEFINES+=TARGET_ccgx"

do_install() {
	install -d ${DEST_DIR}
	install -m 0755 ${S}/dbus_modbustcp ${DEST_DIR}/dbus-modbustcp
	install -m 0644 ${S}/attributes.csv ${DEST_DIR}
	install -m 0644 ${S}/unitid2di.csv ${DEST_DIR}
}


