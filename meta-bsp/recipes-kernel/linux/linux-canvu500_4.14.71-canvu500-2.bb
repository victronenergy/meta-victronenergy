SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel

COMPATIBLE_MACHINE = "canvu500"

RDEPENDS_kernel-base += "kernel-devicetree"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} canvu500_defconfig"

S = "${WORKDIR}/linux-${PV}"
B = "${WORKDIR}/build"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "dac2ea1c91112fa482ba8fad849f94fd"
SRC_URI[sha256sum] = "f77887d9008f8165a195a315e1f3a20f3645c0009e95a58a6e95aee3777b2050"
