DESCRIPTION = "VE system calculations"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit ve_package
inherit daemontools
inherit python-compile

# note: the connected patch is reverted only because it needs to be tested first..
# but we do want a newer version because of dbus bus selection
SRC_URI = " \
	gitsm://github.com/victronenergy/dbus-systemcalc-py.git;protocol=https;tag=${PV} \
	file://com.victronenergy.system.conf \
	file://0001-Revert-In-non-DVCC-systems-update-the-solarchargers-.patch \
	file://0002-Revert-Removed-Shared-Voltage-Sense.patch \
	file://0003-Enable-powering-of-the-loads-or-feeding-in-excess-PV.patch \
"

S = "${WORKDIR}/git"

PR = "r2"

RDEPENDS_${PN} = " \
	localsettings \
	python-dbus \
	python-pprint \
	python-pygobject \
"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_systemcalc.py"

do_install () {
	install -d ${D}${bindir}
	cp -r ${S}/* ${D}${bindir}

	install -d ${D}/${sysconfdir}/dbus-1/system.d
	install -m 644 ${WORKDIR}/com.victronenergy.system.conf ${D}/${sysconfdir}/dbus-1/system.d
}
