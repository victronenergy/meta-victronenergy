DESCRIPTION = "Venus HTML5 logger"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://run file://log.run"

RDEPENDS_${PN} = "gwsocket"

do_install() {
    mkdir -p ${D}/service/venus-html5-app/log
    install -m 755 ${WORKDIR}/run ${D}/service/venus-html5-app/run
    install -m 755 ${WORKDIR}/log.run ${D}/service/venus-html5-app/log/run
}

FILES_${PN} = "/service"
