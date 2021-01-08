LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v12"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "018a2263934027a64b1ba23dfb49faeb"
SRC_URI[sha256sum] = "34112392f1346e680a834c6eff77bbaca62d734988d30991d2941269d2d586c8"

S = "${WORKDIR}/u-boot-${PV}"
