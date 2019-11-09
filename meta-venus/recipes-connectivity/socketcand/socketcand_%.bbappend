FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://start-socketcand"

do_install_append() {
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/start-socketcand ${D}${sbindir}
}

FILES_${PN} += "${sbindir}/start-socketcand"

DAEMONTOOLS_SERVICE_DIR = "${sysconfdir}/${PN}/service"
DAEMONTOOLS_RUN = "${sbindir}/start-socketcand"
DAEMONTOOLS_DOWN = "1"

inherit daemontools
