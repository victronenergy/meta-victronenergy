SRC_URI = "file://resize-sdcard.sh"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS_${PN} = "parted swupdate-scripts"

inherit update-rc.d

INITSCRIPT_PACKAGES = "${PN} ${PN}-resize2fs"
INITSCRIPT_NAME_${PN} = "zzz-resize-sdcard"
INITSCRIPT_PARAMS_${PN} = "start 2 S ."
INITSCRIPT_NAME_${PN}-resize2fs = "resize2fs"
INITSCRIPT_PARAMS_${PN}-resize2fs = "start 40 5 ."

PACKAGES =+ "${PN}-resize2fs"
RDEPENDS_${PN} += "${PN}-resize2fs"
FILES_${PN}-resize2fs = "/etc/init.d/resize2fs"

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 755 ${WORKDIR}/resize-sdcard.sh ${D}${sysconfdir}/init.d/zzz-resize-sdcard

    printf "#!/bin/sh\n\n/opt/victronenergy/swupdate-scripts/resize2fs.sh" > ${WORKDIR}/resize2fs
    install -m 755 ${WORKDIR}/resize2fs ${D}${sysconfdir}/init.d/resize2fs
}
