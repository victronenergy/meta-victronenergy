LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v11"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "f3fe1426388268408b61b4c4fafdb690"
SRC_URI[sha256sum] = "404e0f8c6a12ab3c824ed2f3fb2336cda8003ca3368ae9537cfad5df68b7715e"

S = "${WORKDIR}/u-boot-${PV}"
