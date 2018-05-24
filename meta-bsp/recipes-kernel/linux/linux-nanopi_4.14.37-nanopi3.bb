SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel

COMPATIBLE_MACHINE = "nanopi"

RDEPENDS_kernel-base += "kernel-devicetree"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} nanopi_easysolar_defconfig"
KERNEL_DEVICETREE = "sun8i-h3-nanopi-easysolar.dtb"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "2ae670e241edf5b298dc7c36cdb26e40"
SRC_URI[sha256sum] = "01d4104da5747c66c603c4ff0ba4bfa24cbc22cf793e2604f57d3aaceef23b5e"

S = "${WORKDIR}/linux-${PV}"
