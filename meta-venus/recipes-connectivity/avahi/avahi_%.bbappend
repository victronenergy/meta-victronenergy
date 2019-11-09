FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://avahi-autoipd \
    file://avahi-autoipd.action \
    file://avahi-daemon.conf \
"

INITSCRIPT_PACKAGES += "avahi-autoipd"
INITSCRIPT_NAME_avahi-autoipd = "avahi-autoipd"
INITSCRIPT_PARAMS_avahi-autoipd = "defaults 75 19"

do_install_append() {
    rm -f ${D}${sysconfdir}/avahi/services/*
    install -m 0644 ${WORKDIR}/avahi-daemon.conf ${D}${sysconfdir}/avahi
    install -m 0755 ${WORKDIR}/avahi-autoipd ${D}${INIT_D_DIR}
    install -m 0755 ${WORKDIR}/avahi-autoipd.action ${D}${sysconfdir}/avahi
}

FILES_avahi-autoipd += "${INIT_D_DIR}/avahi-autoipd"
