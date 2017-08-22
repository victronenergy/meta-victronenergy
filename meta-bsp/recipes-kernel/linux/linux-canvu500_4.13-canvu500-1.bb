SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel
require recipes-kernel/linux/linux-dtb.inc

COMPATIBLE_MACHINE = "canvu500"

DEPENDS += "lzop-native"

LINUX_VERSION = "4.13"

S = "${WORKDIR}/linux-${PV}"
B = "${WORKDIR}/build"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "21575fc8847b7fd269ea91b189803509"
SRC_URI[sha256sum] = "848808bca1a4c008b85db0efaf4e31ded6cecacc21d103115d5cfc539fd24648"

SRC_URI += "file://defconfig"
