DESCRIPTION = "DBus to modbus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit qt4e

PR = "r1"

SRC_URI = " \
	https://github.com/victronenergy/dbus_modbustcp/archive/v${PV}.tar.gz \
	file://service \
	"

SRC_URI[md5sum] = "7f76e02afe7a7e8055fb7d73fa33f595"
SRC_URI[sha256sum] = "2f3b43a7f72865b030c86ca7207e1fb1282767329146f81913bc93a39dc24526"

S = "${WORKDIR}/dbus_modbustcp-${PV}"

EXTRA_QMAKEVARS_POST += "DEFINES+=TARGET_ccgx"

BASE_DIR = "/opt/color-control/dbus-modbustcp"
DEST_DIR = "${D}${BASE_DIR}"

do_install() {
	install -d ${DEST_DIR}
	install -m 0755 ${S}/dbus_modbustcp ${DEST_DIR}/dbus-modbustcp
	install -m 0644 ${S}/attributes.csv ${DEST_DIR}
	install -m 0644 ${S}/unitid2di.csv ${DEST_DIR}

	# add service for daemontools
	cp -r ${WORKDIR}/service ${DEST_DIR}
	install -d ${D}/service
	ln -s ${BASE_DIR}/service ${D}/service/dbus-modbustcp
}

FILES_${PN} += "${BASE_DIR}"
FILES_${PN} += "/service"
FILES_${PN}-dbg += "${BASE_DIR}/.debug"

