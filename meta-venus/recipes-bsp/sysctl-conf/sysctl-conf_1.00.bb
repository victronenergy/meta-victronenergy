LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://sysctl.conf"
S = "${WORKDIR}"

do_install () {
    install -m 0755 -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/sysctl.conf ${D}${sysconfdir}
}

