LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v15"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[sha256sum] = "50e32845272ec4022e596ab6dac9916e2932eabcbfa95149fa6769a92c909ea1"

S = "${WORKDIR}/u-boot-${PV}"
