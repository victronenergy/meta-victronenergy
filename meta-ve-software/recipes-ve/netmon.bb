DESCRIPTION = "Network monitor"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit ve_package
inherit daemontools

RDEPENDS_${PN} = "\
    python3-core \
    python3-pyroute2 \
"

SRC_URI = "\
    file://netmon \
"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "${bindir}/${PN}"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/netmon ${D}${bindir}
}
