FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://hostapd.conf \
    file://hostapd-run \
"

inherit daemontools-template

DAEMONTOOLS_RUN = "/usr/sbin/hostapd-run"

# disable rc.d start/stop
INHIBIT_UPDATERCD_BBCLASS = "1"

do_install:append() {
    install -m 0755 ${UNPACKDIR}/hostapd-run ${D}${sbindir}
    install -m 0644 ${UNPACKDIR}/hostapd.conf ${D}${sysconfdir}/hostapd.conf
}
