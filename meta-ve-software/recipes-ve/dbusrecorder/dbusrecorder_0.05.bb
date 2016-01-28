DESCRIPTION = "DBusrecorder"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit allarch

RDEPENDS_${PN} = "python-dbus"
PR = "r0"

SRC_URI = " \
	git://github.com/victronenergy/dbus-recorder.git;protocol=https;tag=${PV} \
	file://startdemo.sh \
	file://stopdemo.sh \
	"

S = "${WORKDIR}/git"
BASE_DIR = "/opt/color-control/dbusrecorder"
DEST_DIR = "${D}${BASE_DIR}"

do_install () {
	install -d ${DEST_DIR}

	install -m 0755 ${WORKDIR}/startdemo.sh ${DEST_DIR}
	install -m 0755 ${WORKDIR}/stopdemo.sh ${DEST_DIR}

	# copy python scripts
	install -m 755 -D ${S}/*.py ${DEST_DIR}
	# copy data files
	install -m 444 -D ${S}/*.dat ${DEST_DIR}
}

FILES_${PN} += "${BASE_DIR}"


