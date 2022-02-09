LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PR = "r1"

INITSCRIPT_NAME = "application"
INITSCRIPT_PARAMS = "start 21 5 2 . stop 80 0 1 6 ."
inherit update-rc.d

S = "${WORKDIR}"

SRC_URI = "file://application"

do_install() {
        install -d ${D}/${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/application ${D}/${sysconfdir}/init.d/
}
