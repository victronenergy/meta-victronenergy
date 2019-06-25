LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PV = "2018.05-sunxi-v5"
SRC_URI = "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "1c9db9c3887ab03007223ca8f4994e2f"
SRC_URI[sha256sum] = "928d1412dab015e9d478b2ec536bc2d37e5b9608ff1f69461b45d49b956701fe"

S = "${WORKDIR}/u-boot-${PV}"
