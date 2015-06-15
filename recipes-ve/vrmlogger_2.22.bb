DESCRIPTION = "Logscript python scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit allarch
inherit ve_package
inherit daemontools

RDEPENDS_${PN} = " \
	localsettings \
	packagegroup-ve-dbus-data-producers \
	python-argparse \
	python-dbus \
	python-fcntl \
	python-pprint \
	python-pygobject \
	python-io \
	python-json \
	python-multiprocessing \
	python-requests \
	python-shell \
	python-sqlite3 \
	"

PR = "r32"
SRC_URI = "gitsm://git.victronenergy.com/ccgx/dbus_vrm.git;protocol=ssh;user=git;tag=vrmlogger-${PV}"
S = "${WORKDIR}/git"
DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 2000000 -s 1000000 -a 100000000 ${bindir}/${PN}.py"

do_install () {
	install -d ${D}${bindir}
	cp -r ${S}/* ${D}${bindir}
}

# cleanup old stuff
pkg_postinst_${PN}() {
	# Running on target?
	if [ "x$D" == "x" ]; then
		echo "Removing logscript leftovers"
		[ -d /data/log/logscript ] && svc -d /service/logscript
		[ -d /service/logscript/log ] && svc -d /service/logscript/log
		[ -d /data/log/logscript ] && rm -rf /data/log/logscript
		[ -f /data/log/logscriptbacklog ] && rm -rf /data/log/logscriptbacklog
		[ -d /opt/color-control/logscript ] && rm -rf /opt/color-control/logscript
		[ -h /service/logscript ] && rm -rf /service/logscript
	fi
}

