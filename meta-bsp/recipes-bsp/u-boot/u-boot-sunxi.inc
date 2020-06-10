LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v9"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "fb5f5724ff4c52c0ab083e48884dd3ad"
SRC_URI[sha256sum] = "a3e6ae2c9706ce5c285ed597dc6fe223348d0c41904bb44eda61034ec0e86c1c"

S = "${WORKDIR}/u-boot-${PV}"
