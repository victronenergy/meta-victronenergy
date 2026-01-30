DESCRIPTION = "U-boot for Cortex-A53 core"

require u-boot.inc
require u-boot-k3.inc

DEPENDS += "\
    dtc-native \
    swig-native \
    u-boot-mkimage-native \
"

DEPENDS += "\
    optee-os \
    ti-linux-firmware \
    trusted-firmware-a \
"

do_deploy[mcdepends] = "mc::k3r5:u-boot-k3r5:do_deploy"

SRC_URI += "file://install.cmds"

inherit firmware

EXTRA_OEMAKE += "BINMAN_INDIRS=${STAGING_FIRMWARE_DIR}"
EXTRA_OEMAKE += "BL31=${STAGING_FIRMWARE_DIR}/bl31.bin"
EXTRA_OEMAKE += "TEE=${STAGING_FIRMWARE_DIR}/tee-raw.bin"

do_compile:append () {
    mkimage -A arm64 -T script -C none -n 'Install Script' \
        -d ${UNPACKDIR}/install.cmds ${UNPACKDIR}/install.scr
}

do_deploy:append () {
    install -d ${DEPLOYDIR}
    install -m 0644 ${UNPACKDIR}/install.scr ${DEPLOYDIR}
}
