FILESEXTRAPATHS:prepend := "${THISDIR}/flashmq:"
SRC_URI += " \
    file://flashmq.conf \
    file://start-flashmq \
"

inherit daemontools-template useradd

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "flashmq"
USERADD_PARAM:${PN} = "--no-create-home --shell /bin/false -g flashmq flashmq"

DAEMONTOOLS_RUN = "${sbindir}/start-flashmq"

RDEPENDS:${PN} += "dbus-flashmq mosquitto-clients"

do_install:append() {
    install ${UNPACKDIR}/flashmq.conf ${D}/${sysconfdir}/flashmq/flashmq.conf

    install -d ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/start-flashmq ${D}${sbindir}

    install -d ${D}${sysconfdir}/default/volatiles
    echo "d root root 0755 /run/flashmq none" > ${D}${sysconfdir}/default/volatiles/50_${PN}
}

FILES:${PN} += "${sysconfdir}/default"
