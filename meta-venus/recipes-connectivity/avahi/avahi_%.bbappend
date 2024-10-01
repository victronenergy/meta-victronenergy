FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGECONFIG += "libdns_sd"

SRC_URI += "\
    file://avahi-autoipd \
    file://avahi-autoipd.action \
    file://avahi-daemon.conf \
    file://0001-do-not-copy-etc-localtime.patch \
    file://0002-patch-service-files-on-start.patch \
"

INITSCRIPT_PACKAGES += "avahi-autoipd"
INITSCRIPT_NAME:avahi-autoipd = "avahi-autoipd"
INITSCRIPT_PARAMS:avahi-autoipd = "defaults 75 19"

do_install:append() {
    rm -f ${D}${sysconfdir}/avahi/services/*
    install -m 0644 ${UNPACKDIR}/avahi-daemon.conf ${D}${sysconfdir}/avahi
    install -m 0755 ${UNPACKDIR}/avahi-autoipd ${D}${INIT_D_DIR}
    install -m 0755 ${UNPACKDIR}/avahi-autoipd.action ${D}${sysconfdir}/avahi

    # move the header file so other things can find it
    mv ${D}${includedir}/avahi-compat-libdns_sd/dns_sd.h ${D}${includedir}
}

FILES:avahi-autoipd += "${INIT_D_DIR}/avahi-autoipd"
