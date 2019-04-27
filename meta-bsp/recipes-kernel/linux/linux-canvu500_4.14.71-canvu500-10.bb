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
SRC_URI[md5sum] = "835e7694d5a6339f25bf05315d723908"
SRC_URI[sha256sum] = "c3c73c55db9c94b7cd45af349e89ab796c29935d55f57e1190f559900950e0d9"
