DESCRIPTION = "PV inverter which gets its information from de VE.Bus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit ve_package
inherit daemontools
inherit python-compile

PR = "r0"
SRC_URI = "gitsm://github.com/victronenergy/dbus_conversions.git;protocol=https;tag=${PV}"
DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_vebus_to_pvinverter.py"
S = "${WORKDIR}/git"
RDEPENDS_${PN} = "python-dbus"

do_install () {
	install -d ${D}${bindir}
	install -m 755 -D ${S}/*.py ${D}${bindir}

	install -d ${D}${bindir}/ext/velib_python
	install ${S}/ext/velib_python/vedbus.py ${D}${bindir}/ext/velib_python
	install ${S}/ext/velib_python/ve_utils.py ${D}${bindir}/ext/velib_python
}

