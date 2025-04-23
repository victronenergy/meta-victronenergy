FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

RDEPENDS:${PN} += "ndisc6-rdisc6"

PACKAGECONFIG = ""
EXTRA_OECONF += "--disable-eaptls --disable-peap"

inherit daemontools

SRC_URI += "\
    file://0001-pppd-Rework-default-route-handling.-544.patch \
    file://ip-updown \
"

DAEMONTOOLS_RUN = "${sbindir}/pppd call provider nodetach"
DAEMONTOOLS_DOWN = "1"

do_install:append() {
    # remove useless files
    rm -fr ${D}${sysconfdir}/chatscripts
    rm -f ${D}${sysconfdir}/init.d/ppp
    rm -f ${D}${sysconfdir}/ppp/*.example
    rm -f ${D}${sysconfdir}/ppp/ppp_on_boot
    rm -fr ${D}${sysconfdir}/ppp/ip-up.d
    rm -fr ${D}${sysconfdir}/ppp/ip-down.d

    install -m 0755 ${UNPACKDIR}/ip-updown ${D}${sysconfdir}/ppp/
    ln -sf ip-updown ${D}${sysconfdir}/ppp/ip-up
    ln -sf ip-updown ${D}${sysconfdir}/ppp/ip-down
    ln -sf ip-updown ${D}${sysconfdir}/ppp/ipv6-up
    ln -sf ip-updown ${D}${sysconfdir}/ppp/ipv6-down
}
