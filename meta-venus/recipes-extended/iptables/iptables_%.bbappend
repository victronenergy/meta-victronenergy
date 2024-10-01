FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://firewall \
    file://init \
    file://rules \
"

inherit update-rc.d

INITSCRIPT_NAME = "iptables"
INITSCRIPT_PARAMS = "start 50 S ."

do_install:append() {
    install -d ${D}${sysconfdir}/iptables
    install -m 0644 ${UNPACKDIR}/rules ${D}${sysconfdir}/iptables

    install -d ${D}${INIT_D_DIR}
    install -m 0755 ${UNPACKDIR}/init ${D}${INIT_D_DIR}/iptables

    install -m 0755 ${UNPACKDIR}/firewall ${D}${sbindir}
}
