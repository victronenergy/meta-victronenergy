DESCRIPTION = "VE character display server for easysolar"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit ve_package
inherit daemontools
inherit python-compile

COMPATIBLE_MACHINE = "nanopi"

SRCREV = "1d1c6a99da94b4f9ed6992c9f5b94b6e5c1266d1"

SRC_URI = " \
	gitsm://github.com/victronenergy/dbus-characterdisplay.git;protocol=ssh;user=git;branch=master \
"

S = "${WORKDIR}/git"

RDEPENDS_${PN} = " \
	dbus-systemcalc-py \
	localsettings \
	python-dbus \
"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_characterdisplay.py"

do_install () {
	install -d ${D}${bindir}
	install -m 0755 ${S}/dbus_characterdisplay.py ${D}${bindir}
	install -m 0644 ${S}/lcddriver.py ${D}${bindir}
}
