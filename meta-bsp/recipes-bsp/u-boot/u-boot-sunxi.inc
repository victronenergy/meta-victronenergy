LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v8"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "793157c37e76234278356c152aca19d4"
SRC_URI[sha256sum] = "22ae2148267b71d58906272a877e23d7bb22d880b07bc27cfff9d9114db5ab7e"

S = "${WORKDIR}/u-boot-${PV}"
