SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel

COMPATIBLE_MACHINE = "nanopi"

RDEPENDS_kernel-base += "kernel-devicetree"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} nanopi_victron_defconfig"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "f96338e142685734de0716e2333065a7"
SRC_URI[sha256sum] = "d8f442b8b9d69995e794e63ebf8147288a6ee5b5722ff58b5c9f7269a9a6fbdd"

S = "${WORKDIR}/linux-${PV}"
