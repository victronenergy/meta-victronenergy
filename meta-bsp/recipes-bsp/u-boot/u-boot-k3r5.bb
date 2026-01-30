DESCRIPTION = "U-Boot for Cortex-R5 core"

require u-boot.inc
require u-boot-k3.inc

COMPATIBLE_MACHINE = "^k3r5$"

DEPENDS += "\
    dtc-native \
    swig-native \
"

DEPENDS += "\
    ti-linux-firmware \
"

inherit firmware

EXTRA_OEMAKE += "BINMAN_INDIRS=${STAGING_FIRMWARE_DIR}"
