SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel
require recipes-kernel/linux/linux-dtb.inc

COMPATIBLE_MACHINE = "canvu500"

DEPENDS += "lzop-native"

LINUX_VERSION = "4.14"

S = "${WORKDIR}/linux-${PV}"
B = "${WORKDIR}/build"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "055e038f1510796a89cb9c60fd5e33b4"
SRC_URI[sha256sum] = "570f4a50d6ca5d2ea2eaaaf1b56517b20ef51d72cd376f1164fe484c79d13b7e"

SRC_URI += "file://defconfig"
