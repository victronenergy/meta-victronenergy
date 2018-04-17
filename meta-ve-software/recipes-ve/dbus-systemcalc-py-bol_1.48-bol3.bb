DESCRIPTION = "VE system calculations"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit ve_package
inherit daemontools
inherit python-compile

# note: the connected patch is reverted only because it needs to be tested first..
# but we do want a newer version because of dbus bus selection
SRC_URI = "gitsm://github.com/victronenergy/dbus-systemcalc-py.git;protocol=https;branch=bol2;tag=${PV}"

S = "${WORKDIR}/git"

RDEPENDS_${PN} = " \
	localsettings \
	python-dbus \
	python-pprint \
	python-pygobject \
"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_systemcalc.py"
DAEMONTOOLS_DOWN = "1"

do_install () {
	install -d ${D}${bindir}
	cp -r ${S}/* ${D}${bindir}
}
