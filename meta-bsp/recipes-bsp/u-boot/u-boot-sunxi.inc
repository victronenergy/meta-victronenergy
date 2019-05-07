LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v4"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "f7a78882c2661f5c5356771838abe5b5"
SRC_URI[sha256sum] = "8d1938e9cfb8c7affa42a45964d38641d1293254cf4f4b6a592cfd2dd81d5c17"

S = "${WORKDIR}/u-boot-${PV}"
