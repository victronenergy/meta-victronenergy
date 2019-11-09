FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://0001-slcand-daemonise-only-once-setup-is-complete.patch \
    file://can-set-rate \
"

do_install_append() {
    install -m 0755 ${WORKDIR}/can-set-rate ${D}${bindir}
}
