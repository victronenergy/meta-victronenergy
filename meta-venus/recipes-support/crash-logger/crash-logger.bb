DESCRIPTION = "Process crash logger"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INITSCRIPT_NAME = "crash-logger"
INITSCRIPT_PARAMS = "start 90 5 . stop 10 0 6 ."
inherit update-rc.d

SRC_URI = "\
    file://core-handler \
    file://crash-logger.init \
"

do_install () {
    install -d ${D}${base_sbindir}
    install -m 0755 ${UNPACKDIR}/core-handler ${D}${base_sbindir}

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/crash-logger.init ${D}${sysconfdir}/init.d/crash-logger
}
