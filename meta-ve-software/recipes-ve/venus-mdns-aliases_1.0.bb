SUMMARY = "Publish a static mDNS alias for GX devices"
DESCRIPTION = "Connect to avahi over the dbus to add a CNAME entry. \
Unfortunately avahi doesn't have a config file for this."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://venus-mdns-aliases.c;subdir=${S} \
    file://init.rc \
"
S = "${UNPACKDIR}/recipe-src"

DEPENDS = "avahi"
RDEPENDS:${PN} = "avahi-daemon"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "${PN}.sh"
INITSCRIPT_PARAMS:${PN} = "start 99 5 . stop 1 6 ."

inherit update-rc.d

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} ${S}/venus-mdns-aliases.c \
        -lavahi-client -lavahi-common -o venus-mdns-aliases
}

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${B}/venus-mdns-aliases ${D}${sbindir}

    install -d ${D}/${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/init.rc ${D}/${sysconfdir}/init.d/${PN}.sh
}
