FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://ccgx-ca.crt"

do_install:prepend () {
    install -d ${D}${datadir}/ca-certificates/victronenergy
    install ${UNPACKDIR}/ccgx-ca.crt ${D}${datadir}/ca-certificates/victronenergy
}
