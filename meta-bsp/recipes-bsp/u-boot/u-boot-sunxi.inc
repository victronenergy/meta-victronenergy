LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v10"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "68e6f980a4f0f54387252bb76f23b4f0"
SRC_URI[sha256sum] = "83665319b1581ec90674717841ce9c0813220d39cff907bdf7e2833c1adef812"

S = "${WORKDIR}/u-boot-${PV}"
