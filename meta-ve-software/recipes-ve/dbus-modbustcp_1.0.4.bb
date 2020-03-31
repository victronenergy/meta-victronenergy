DESCRIPTION = "DBus to modbus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit qmakeve
inherit daemontools

SRC_URI = " \
    gitsm://github.com/victronenergy/dbus_modbustcp.git;tag=v${PV};protocol=https \
"
S = "${WORKDIR}/git"
DEST_DIR = "${D}${bindir}"
DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/${PN}"
DAEMONTOOLS_DOWN = "1"

# why? dbus connection, will be fixed when switching to common code..
EXTRA_QMAKEVARS_POST += "DEFINES+=TARGET_ccgx"

do_install_append () {
    install -m 0644 ${S}/attributes.csv ${D}${bindir}
    install -m 0644 ${S}/unitid2di.csv ${D}${bindir}
}


