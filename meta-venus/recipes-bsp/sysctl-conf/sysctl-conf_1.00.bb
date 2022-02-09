LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://sysctl.conf"
S = "${WORKDIR}"

do_install () {
    install -m 0755 -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/sysctl.conf ${D}${sysconfdir}
}

