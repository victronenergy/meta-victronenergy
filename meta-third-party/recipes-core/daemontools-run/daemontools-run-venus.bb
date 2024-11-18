LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INITSCRIPT_NAME = "svscanboot.sh"
INITSCRIPT_PARAMS = "start 95 5 2 . stop 15 6 ."
inherit update-rc.d

S = "${WORKDIR}"
RDEPENDS:${PN} += "daemontools"

SRC_URI += "file://svscanboot.sh"

do_install () {
    mkdir -p ${D}/${sysconfdir}/init.d
    install -D ${WORKDIR}/svscanboot.sh ${D}/${sysconfdir}/init.d
}

