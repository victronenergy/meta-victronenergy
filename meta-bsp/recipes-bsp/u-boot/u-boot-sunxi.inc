LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v6"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "26ee577a184a94ce089526f70850c15a"
SRC_URI[sha256sum] = "d348968cde2a76074991ab4689d2e542d3d1d2c147973a2cf16e1618c04b7cb2"

S = "${WORKDIR}/u-boot-${PV}"
