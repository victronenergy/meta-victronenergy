LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v21"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[sha256sum] = "b15f1bf8dee373c68ffd922e9ff00360606397a351bb82e31ffc48f4672d0157"
INSANE_SKIP += "src-uri-bad"

S = "${UNPACKDIR}/u-boot-${PV}"
