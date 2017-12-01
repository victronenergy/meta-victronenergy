SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel

COMPATIBLE_MACHINE = "nanopi"

RDEPENDS_kernel-base += "kernel-devicetree"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} nanopi_easysolar_defconfig"
KERNEL_DEVICETREE = "sun8i-h3-nanopi-easysolar.dtb"

SRC_URI = "git://github.com/victronenergy/linux.git;protocol=https;branch=nanopi_b4.14.37"
SRCREV = "ed713476da8f33f53a025557873aa253ae53d9bc"

S = "${WORKDIR}/git"
