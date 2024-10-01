DESCRIPTION = "creates / updates the permanent storage (/data)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://volatiles"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

do_install() {
    install -d ${D}${sysconfdir}/default/volatiles
    install -m 0644 ${UNPACKDIR}/volatiles ${D}${sysconfdir}/default/volatiles/30_${PN}
}

FILES:${PN} = "${sysconfdir}"
