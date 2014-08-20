DESCRIPTION = "DBus to modbus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit qt4e

PR = "r0"

BB_FETCH_PREMIRRORONLY = "0"
PREMIRRORS = ""

SRC_URI = " \
	https://github.com/victronenergy/dbus_modbustcp/archive/v${PV}.tar.gz \
	file://service \
	"

SRC_URI[md5sum] = "36387258d66acb0064a0b1f7a5c2a3ae"
SRC_URI[sha256sum] = "96bddec52a43e07ae3ae6be1c0b244d6b7eb6b8d9014ac96d20d80d3958d410b"

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

