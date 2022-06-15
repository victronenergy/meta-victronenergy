LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v13"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[sha256sum] = "1e83e552ba62dfc4f7cc30e80146c3dbfc0eb602f2ba4e4b6405f67e701b50a2"

S = "${WORKDIR}/u-boot-${PV}"
