FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://can-set-rate \
"

do_install_append() {
    install -m 0755 ${WORKDIR}/can-set-rate ${D}${bindir}
}
