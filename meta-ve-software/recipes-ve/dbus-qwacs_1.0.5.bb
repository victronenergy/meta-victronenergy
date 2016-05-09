DESCRIPTION = "QT console application to present Quby wireless AC sensors on the DBus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit qt4e

RDEPENDS_${PN} = "dbus"
PR = "r0"
SRC_URI = "https://github.com/victronenergy/dbus_qwacs/archive/v${PV}.tar.gz \
           file://service \
          "
SRC_URI[md5sum] = "4c2d97f93679e6d0c8a5c5c4af624327"
SRC_URI[sha256sum] = "f8685080ec13e90fab063048a931f757819ca99f659e52582f57a7000c32113d"

S = "${WORKDIR}/dbus_qwacs-${PV}"

EXTRA_QMAKEVARS_POST += "DEFINES+=TARGET_ccgx"

BASE_DIR = "/opt/color-control/dbus-qwacs"
DEST_DIR = "${D}${BASE_DIR}"

do_install() {
	install -d ${DEST_DIR}
	install -m 0755 ${S}/dbus_qwacs ${DEST_DIR}/dbus-qwacs

	# add service for daemontools
	cp -r ${WORKDIR}/service ${DEST_DIR}

	install -d ${D}/service
	ln -s ${BASE_DIR}/service ${D}/service/dbus-qwacs
}

FILES_${PN} += "${BASE_DIR}"
FILES_${PN} += "/service"
FILES_${PN}-dbg += "${BASE_DIR}/.debug"

