LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v19"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[sha256sum] = "6271746b0ac981e4c3f0f44174bfc7bee634a1506e386c78c2da2782c8c7d048"
INSANE_SKIP += "src-uri-bad"

S = "${UNPACKDIR}/u-boot-${PV}"
