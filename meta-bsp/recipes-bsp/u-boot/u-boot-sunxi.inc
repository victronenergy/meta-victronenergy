LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v7"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "5217bf65192b2b7986c9e86ae6354cd3"
SRC_URI[sha256sum] = "69e6c1ec31bc3e85ad4f372bb5678a568cbc40e7f61154d47c6600a54310a2d4"

S = "${WORKDIR}/u-boot-${PV}"
