DESCRIPTION = "Network monitor"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit ve_package
inherit daemontools

RDEPENDS:${PN} = "\
    python3-core \
    python3-pyroute2 \
"

SRC_URI = "\
    file://netmon \
"

DAEMONTOOLS_RUN = "${bindir}/${PN}"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/netmon ${D}${bindir}
}
