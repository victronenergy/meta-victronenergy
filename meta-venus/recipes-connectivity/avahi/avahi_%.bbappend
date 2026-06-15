FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGECONFIG += "libdns_sd"

SRC_URI += "\
    file://avahi-autoipd.action \
    file://avahi-daemon.conf \
    file://start-avahi-autoipd \
    file://victron-mqtt.service.in \
    file://0001-do-not-copy-etc-localtime.patch \
    file://0002-patch-service-files-on-start.patch \
    file://0003-publish-mdns-aliases.patch \
"

DAEMON_PN = "avahi-autoipd"
DAEMONTOOLS_SCRIPT = "exec ${sbindir}/start-avahi-autoipd"

# should be the default actually...
DAEMONTOOLS_SERVICE_DIR = "${DAEMONTOOLS_COMMON_TEMPLATES_DIR}/${DAEMON_PN}"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/${DAEMON_PN}"

inherit daemontools

do_install:append() {
    rm -f ${D}${sysconfdir}/avahi/services/*
    install -m 0644 ${UNPACKDIR}/avahi-daemon.conf ${D}${sysconfdir}/avahi
    install -m 0755 ${UNPACKDIR}/avahi-autoipd.action ${D}${sysconfdir}/avahi

    # move the header file so other things can find it
    mv ${D}${includedir}/avahi-compat-libdns_sd/dns_sd.h ${D}${includedir}

    install -m755 "${UNPACKDIR}/start-avahi-autoipd" ${D}${sbindir}
    install -m644 "${UNPACKDIR}/victron-mqtt.service.in" ${D}${sysconfdir}/avahi/services/
    ln -sf /run/avahi/services/victron-mqtt.service ${D}${sysconfdir}/avahi/services/victron-mqtt.service

    # make this the default as well.
    mkdir -p "${D}/${DAEMONTOOLS_TEMPLATE_CONF_DIR}"
    printf "${DAEMONTOOLS_TEMPLATE_CONF}\n" > "${D}/${DAEMONTOOLS_TEMPLATE_CONF_DIR}/${DAEMON_PN}.conf"
}

FILES:avahi-autoipd += "${sbindir}/start-avahi-autoipd"
FILES:avahi-daemon += "${sysconfdir}/avahi/services/victron-mqtt.service"
FILES:avahi-daemon += "${sysconfdir}/avahi/services/victron-mqtt.service.in"

RDEPENDS:avahi-daemon += " venus-mdns-aliases"
