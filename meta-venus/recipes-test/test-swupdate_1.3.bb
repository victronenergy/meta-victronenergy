LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

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
