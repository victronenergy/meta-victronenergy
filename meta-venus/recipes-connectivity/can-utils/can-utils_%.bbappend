FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://can-set-rate \
"

do_install:append() {
    install -m 0755 ${UNPACKDIR}/can-set-rate ${D}${bindir}
}
