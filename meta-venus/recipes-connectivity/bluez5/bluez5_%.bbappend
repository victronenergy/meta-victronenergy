FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PACKAGECONFIG = "deprecated readline tools"

SRC_URI += "\
	file://ble.conf \
	file://init.default \
"

do_install_append() {
	install -m 0644 ${WORKDIR}/ble.conf ${D}${sysconfdir}/bluetooth

	install -d ${D}${sysconfdir}/default
	install -m 0644 ${WORKDIR}/init.default ${D}${sysconfdir}/default/bluetooth
}
