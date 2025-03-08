FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit update-rc.d

SRC_URI += "file://haveged"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "haveged"
INITSCRIPT_PARAMS:${PN} = "start 38 S . stop 09 0 6 ."

do_install:append() {
    install -d ${D}${INIT_D_DIR}
    install -m 755 ${UNPACKDIR}/haveged ${D}${INIT_D_DIR}/haveged
}
