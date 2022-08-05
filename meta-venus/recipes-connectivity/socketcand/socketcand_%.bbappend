FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://start-socketcand"

do_install:append() {
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/start-socketcand ${D}${sbindir}
}

FILES:${PN} += "${sbindir}/start-socketcand"

DAEMONTOOLS_RUN = "${sbindir}/start-socketcand"

inherit daemontools-template
