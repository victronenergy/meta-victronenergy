LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v20"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[sha256sum] = "44a256dc62275ae113c80c9c314d0b7bff11159b8710ff299115e5473c87f023"
INSANE_SKIP += "src-uri-bad"

S = "${UNPACKDIR}/u-boot-${PV}"
