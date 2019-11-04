DESCRIPTION = "Firmware update scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit ve_package

RDEPENDS_${PN} = "\
    machine-runtime-conf \
"

RDEPENDS_${PN}_append_einstein = "\
    openocd \
"

SRC_URI = "\
    file://venus-firmware-update \
"

SRC_URI_append_einstein = "\
    file://cerbogx-samc21-can-adc.cfg \
"

SCRIPTDIR = "/usr/share/openocd/site"
FILES_${PN} += "${SCRIPTDIR}"

do_install() {
    install -m 0755 -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/venus-firmware-update ${D}${bindir}
}

do_install_append_einstein() {
    install -m 0755 -d ${D}${SCRIPTDIR}
    install -m 0644 ${WORKDIR}/cerbogx-samc21-can-adc.cfg ${D}${SCRIPTDIR}
}
