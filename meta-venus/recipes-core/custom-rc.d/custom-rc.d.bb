SUMMARY = "Call user-defined scripts at startup, if they exist"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = " \
    file://custom-rc-early.sh \
    file://custom-rc-late.sh \
"

S = "${WORKDIR}"

inherit allarch
inherit update-rc.d

INITSCRIPT_PACKAGES = "${PN}-early ${PN}-late"
INITSCRIPT_NAME:${PN}-early = "custom-rc-early.sh"
INITSCRIPT_PARAMS:${PN}-early = "start 99 S ."
INITSCRIPT_NAME:${PN}-late = "custom-rc-late.sh"
INITSCRIPT_PARAMS:${PN}-late = "start 99 5 . stop 1 6 ."

PACKAGES =+ "${PN}-early ${PN}-late"

FILES:${PN}-early = "/etc/init.d/custom-rc-early.sh"
FILES:${PN}-late = "/etc/init.d/custom-rc-late.sh"

RDEPENDS:${PN} += "${PN}-early ${PN}-late"
ALLOW_EMPTY:${PN} = "1"

do_install () {
    install -d ${D}${sysconfdir}/rcS.d
    install -d ${D}${sysconfdir}/rc5.d
    install -d ${D}${sysconfdir}/init.d

    install -m 0755 ${WORKDIR}/custom-rc-early.sh    ${D}${sysconfdir}/init.d/custom-rc-early.sh
    install -m 0755 ${WORKDIR}/custom-rc-late.sh    ${D}${sysconfdir}/init.d/custom-rc-late.sh
}
