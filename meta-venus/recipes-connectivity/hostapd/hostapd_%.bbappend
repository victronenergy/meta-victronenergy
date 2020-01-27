FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://hostapd.conf \
    file://hostapd-run \
"

inherit daemontools

DAEMONTOOLS_SERVICE_DIR = "${sysconfdir}/${PN}/service"
DAEMONTOOLS_RUN = "/usr/sbin/hostapd-run"
DAEMONTOOLS_DOWN = "1"

# disable rc.d start/stop
INHIBIT_UPDATERCD_BBCLASS = "1"

do_install_append() {
    install -m 0755 ${WORKDIR}/hostapd-run ${D}${sbindir}
    install -m 0644 ${WORKDIR}/hostapd.conf ${D}${sysconfdir}/hostapd.conf
}
