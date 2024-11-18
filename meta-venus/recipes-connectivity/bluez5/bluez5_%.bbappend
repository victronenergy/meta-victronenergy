FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGECONFIG = "deprecated readline tools"

SRC_URI += "\
    file://ble.conf \
    file://init.default \
"

EXTRA_OECONF += "\
    --localstatedir=/data/var \
"

do_install:append() {
    install -m 0644 ${UNPACKDIR}/ble.conf ${D}${sysconfdir}/bluetooth

    install -d ${D}${sysconfdir}/default
    install -m 0644 ${UNPACKDIR}/init.default ${D}${sysconfdir}/default/bluetooth

    rm -rf ${D}/data
}
