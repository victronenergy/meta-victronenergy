LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5788eada6cf655ecefcba0bcac5add84"

inherit ve_package
inherit daemontools

# ccgx only, hardcoded gpio pins!
inherit velib_target

SRC_URI = "gitsm://github.com/victronenergy/dbus_generator.git;protocol=https;tag=v${PV}"
PR = "r0"
S = "${WORKDIR}/git"

RDEPENDS_${PN} = " \
	localsettings \
	python-argparse \
	python-datetime \
	python-dbus \
	python-json \
	python-pygobject \
"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 2000000 -s 1000000 -a 100000000 ${bindir}/dbus_generator.py"
DAEMONTOOLS_DOWN = "1"
 
do_install () {
	install -d ${D}${bindir}
	cp -r ${S}/* ${D}${bindir}
}
