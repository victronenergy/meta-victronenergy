DESCRIPTION = "GSM/3G modem manager"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit ve_package
inherit daemontools
inherit python-compile

RDEPENDS_${PN} = "\
	bash \
	python-dbus \
	python-pygobject \
	python-pyserial \
"

SRC_URI = " \
	gitsm://github.com/victronenergy/dbus-modem.git;protocol=https;tag=v${PV} \
	file://start-modem.sh \
	file://reset-modem.sh \
"
S = "${WORKDIR}/git"

DAEMONTOOLS_RUN = "${bindir}/start-modem.sh TTY"
DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}.TTY"
DAEMONTOOLS_SERVICE_SYMLINK = "0"
DAEMONTOOLS_DOWN = "1"

do_install () {
	install -d ${D}${bindir}
	install -m 0755 ${S}/dbus-modem.py ${D}${bindir}
	install -m 0755 ${WORKDIR}/start-modem.sh ${D}${bindir}
	install -m 0755 ${WORKDIR}/reset-modem.sh ${D}${bindir}

	for f in settingsdevice ve_utils vedbus; do
		install -m 0644 ${S}/ext/velib_python/$f.py ${D}${bindir}
	done
}
