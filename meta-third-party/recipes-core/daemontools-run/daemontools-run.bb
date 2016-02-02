LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

INITSCRIPT_NAME = "svscanboot.sh"
INITSCRIPT_PARAMS = "start 30 5 2 ."
inherit update-rc.d

S = "${WORKDIR}"
RDEPENDS_${PN} += "daemontools"

SRC_URI += "file://svscanboot.sh"

do_install () {
	mkdir -p ${D}/${sysconfdir}/init.d
	install -D ${WORKDIR}/svscanboot.sh ${D}/${sysconfdir}/init.d
}

