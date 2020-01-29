SUMMARY = "Call user-defined scripts at startup, if they exist"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = " \
    file://custom-rc-early.sh \
    file://custom-rc-late.sh \
"

S = "${WORKDIR}"

inherit allarch
inherit update-rc.d

INITSCRIPT_PACKAGES = "${PN}-early ${PN}-late"
INITSCRIPT_NAME_${PN}-early = "custom-rc-early.sh"
INITSCRIPT_PARAMS_${PN}-early = "start 99 S ."
INITSCRIPT_NAME_${PN}-late = "custom-rc-late.sh"
INITSCRIPT_PARAMS_${PN}-late = "start 99 5 . stop 1 6 ."

PACKAGES =+ "${PN}-early ${PN}-late"

FILES_${PN}-early = "/etc/init.d/custom-rc-early.sh"
FILES_${PN}-late = "/etc/init.d/custom-rc-late.sh"

RDEPENDS_${PN} += "${PN}-early ${PN}-late"
ALLOW_EMPTY_${PN} = "1"

do_install () {
    install -d ${D}${sysconfdir}/rcS.d
    install -d ${D}${sysconfdir}/rc5.d
    install -d ${D}${sysconfdir}/init.d

    install -m 0755 ${WORKDIR}/custom-rc-early.sh    ${D}${sysconfdir}/init.d/custom-rc-early.sh
    install -m 0755 ${WORKDIR}/custom-rc-late.sh    ${D}${sysconfdir}/init.d/custom-rc-late.sh
}
