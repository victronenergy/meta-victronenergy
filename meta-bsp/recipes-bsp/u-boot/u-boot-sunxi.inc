LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v16"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[sha256sum] = "e659eb82b24c002a167083588dda8fd3d643fc09c41bb9dbdd26cf64833ce6a5"

S = "${WORKDIR}/u-boot-${PV}"
