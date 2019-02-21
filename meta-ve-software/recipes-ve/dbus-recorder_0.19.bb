DESCRIPTION = "DBusrecorder"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit ve_package
inherit python-compile

RDEPENDS_${PN} = "bash python-dbus"

SRC_URI = "git://github.com/victronenergy/dbus-recorder.git;protocol=https;tag=${PV}"
S = "${WORKDIR}/git"

do_install () {
	install -d ${D}/${bindir}

	install -m 0755 ${S}/play.sh ${D}/${bindir}
	install -m 0755 ${S}/startdemo.sh ${D}/${bindir}
	install -m 0755 ${S}/stopdemo.sh ${D}/${bindir}

	# copy python scripts
	install -m 755 -D ${S}/*.py ${D}/${bindir}
	# copy data files
	install -m 444 -D ${S}/*.dat ${D}/${bindir}
}
