SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel

COMPATIBLE_MACHINE = "sunxi"

RDEPENDS_kernel-base += "kernel-devicetree"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} sunxi_victron_defconfig"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "80677734e4a063a1d18235813aa2fd86"
SRC_URI[sha256sum] = "fbe3742f04b1769dcb909da7b5758f4586a6377f21f295d2b915ba9340f9b8a5"

S = "${WORKDIR}/linux-${PV}"
