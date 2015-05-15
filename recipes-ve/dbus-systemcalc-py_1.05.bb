DESCRIPTION = "VE system calculations"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit ve_package
inherit daemontools

SRC_URI = "gitsm://github.com/victronenergy/dbus-systemcalc-py.git;protocol=https;tag=${PV}"
PR = "r21"
S = "${WORKDIR}/git"
RDEPENDS = "python-dbus"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 2000000 -s 1000000 -a 100000000 ${bindir}/dbus_systemcalc.py"

do_install () {
	install -d ${D}${bindir}
	cp -r ${S}/* ${D}${bindir}
}

