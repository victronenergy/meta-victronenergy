LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit ve_package
inherit daemontools
inherit python-compile

SRC_URI = " \
    gitsm://github.com/victronenergy/dbus_pump.git;protocol=https;tag=v${PV} \
    file://com.victronenergy.pump.conf \
"
PR = "r1"
S = "${WORKDIR}/git"

RDEPENDS_${PN} = " \
    localsettings \
    python \
    python-argparse \
    python-datetime \
    python-dbus \
    python-json \
    python-pygobject \
"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_pump.py"
DAEMONTOOLS_DOWN = "1"

do_install () {
    install -d ${D}${bindir}
    cp -r ${S}/* ${D}${bindir}

    install -d ${D}/${sysconfdir}/dbus-1/system.d
    install -m 644 ${WORKDIR}/com.victronenergy.pump.conf ${D}/${sysconfdir}/dbus-1/system.d
}
