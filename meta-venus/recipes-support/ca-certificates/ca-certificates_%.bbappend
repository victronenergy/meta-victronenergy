FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://ccgx-ca.crt"

do_install_prepend () {
    install -d ${D}${datadir}/ca-certificates/victronenergy
    install ${WORKDIR}/ccgx-ca.crt ${D}${datadir}/ca-certificates/victronenergy
}
