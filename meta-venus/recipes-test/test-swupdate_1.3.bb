LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SUMMARY = "endless update loop"

INITSCRIPT_NAME = "test-swupdate.sh"
INITSCRIPT_PARAMS = "start 99 5 2 ."
inherit update-rc.d


S = "${WORKDIR}"

SRC_URI = "file://test-swupdate.sh"

do_install() {
        install -d ${D}/${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/test-swupdate.sh ${D}/${sysconfdir}/init.d/
}
