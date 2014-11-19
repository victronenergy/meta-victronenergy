LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r1"

INITSCRIPT_NAME = "application"
INITSCRIPT_PARAMS = "start 21 5 2 . stop 80 0 1 6 ."
inherit update-rc.d

SRC_URI = "file://application"

do_install() {
        install -d ${D}/${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/application ${D}/${sysconfdir}/init.d/
}
