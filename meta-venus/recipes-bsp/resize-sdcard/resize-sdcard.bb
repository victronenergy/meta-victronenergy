SRC_URI = "file://resize-sdcard.sh"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS:${PN} = "parted swupdate-scripts"

inherit update-rc.d

# Not running automatically, since there is a readonly rootfs now.
# Add ${PN}-resize2fs to INITSCRIPT_PACKAGES to run it during boot.
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "zzz-resize-sdcard"
INITSCRIPT_PARAMS:${PN} = "start 2 S ."
INITSCRIPT_NAME:${PN}-resize2fs = "resize2fs"
INITSCRIPT_PARAMS:${PN}-resize2fs = "start 40 5 ."

PACKAGES =+ "${PN}-resize2fs"
RDEPENDS:${PN} += "${PN}-resize2fs"
FILES:${PN}-resize2fs = "/etc/init.d/resize2fs"

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 755 ${WORKDIR}/resize-sdcard.sh ${D}${sysconfdir}/init.d/zzz-resize-sdcard

    printf "#!/bin/sh\n\n/opt/victronenergy/swupdate-scripts/resize2fs.sh" > ${WORKDIR}/resize2fs
    install -m 755 ${WORKDIR}/resize2fs ${D}${sysconfdir}/init.d/resize2fs
}
