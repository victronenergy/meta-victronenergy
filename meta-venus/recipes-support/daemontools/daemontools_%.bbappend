FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://0001-scan-on-SIGALRM.patch \
    file://svrescan \
"

do_install:append() {
    install -m 755 ${UNPACKDIR}/svrescan ${D}/${bindir}
}
