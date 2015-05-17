require u-boot.inc

LIC_FILES_CHKSUM = "file://Licenses/README;md5=c7383a594871c03da76b3707929d2919"
SRC_URI = "https://github.com/jhofstee/u-boot/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "6deb47d577757f00a9a220e4a4eb38f0"
SRC_URI[sha256sum] = "7d59e27508525398c68a2e367f12f42f8c37951fa577942678e50d0446d1acef"
S = "${WORKDIR}/u-boot-${PV}"
PR = "r1"
UBOOT_IMAGE = "u-boot.imx"
