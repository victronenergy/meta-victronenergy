SUMMARY = "Publish predictable mDNS aliases for GX devices"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://venus-mdns-aliases.c;subdir=${S}"
S = "${UNPACKDIR}/recipe-src"

DEPENDS = "avahi"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} ${S}/venus-mdns-aliases.c \
        -lavahi-client -lavahi-common -o venus-mdns-aliases
}

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${B}/venus-mdns-aliases ${D}${sbindir}
}
