DESCRIPTION = "Localsettings python scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit allarch
inherit ve_package
inherit daemontools

PR = "r12"
SRC_URI = " \
	gitsm://github.com/victronenergy/localsettings.git;protocol=https;tag=v${PV} \
	file://set_setting.sh \
	"
S = "${WORKDIR}/git"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 2000000 -s 1000000 -a 100000000 ${bindir}/${PN}.py"

RDEPENDS_${PN} += " \
	python-dbus \
	python-lxml \
"

do_install () {
	install -d ${D}${bindir}
	install -m 755 -D ${S}/*.py ${D}${bindir}
	install -m 755 -D ${WORKDIR}/set_setting.sh ${D}${bindir}

	install -d ${D}${bindir}/ext/velib_python
	install -m 644 ${S}/ext/velib_python/tracing.py ${D}${bindir}/ext/velib_python
}

# remember the version updated from ...
pkg_preinst_${PN} () {
	if [ "x$D" = "x" ]; then
		cp ${bindir}/../version ${bindir}/previous_version
	fi
}

pkg_postinst_${PN} () {
	if [ "x$D" = "x" ]; then
		# version upto v1.15 would set access level to user, but did not
		# enforce any policy. Since v1.16 users are locked in a user level
		# so make sure the default is changed to to User & Installer when
		# updating from an older version.
		${bindir}/set_setting.sh AccessLevel 1 115
	fi
}

