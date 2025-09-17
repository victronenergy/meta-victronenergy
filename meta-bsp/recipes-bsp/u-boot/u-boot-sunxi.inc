LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v18"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[sha256sum] = "9d218cba2279de0e569a4035fb899f268eb28ba8cef867fe093d2652ed46025e"
INSANE_SKIP += "src-uri-bad"

S = "${UNPACKDIR}/u-boot-${PV}"
