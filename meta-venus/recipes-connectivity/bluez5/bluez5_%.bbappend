FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PACKAGECONFIG = "deprecated readline tools"

SRC_URI += "\
    file://disable_gatt_reverse_service_discovery.patch \
    file://ble.conf \
    file://init.default \
"

EXTRA_OECONF += "\
    --localstatedir=/data/var \
"

do_install_append() {
    install -m 0644 ${WORKDIR}/ble.conf ${D}${sysconfdir}/bluetooth

    install -d ${D}${sysconfdir}/default
    install -m 0644 ${WORKDIR}/init.default ${D}${sysconfdir}/default/bluetooth
}
