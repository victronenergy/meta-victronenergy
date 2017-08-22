require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "canvu500"

UBOOT_LOCALVERSION = "-venus"

SRC_URI = "\
	https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz \
"
SRC_URI[md5sum] = "adb5ac4d36038c00a98bd54df03808a7"
SRC_URI[sha256sum] = "66261bc02c11e9f36f527fa807ab79858dccd10bd42ca15c4bfc8c26f2c64c89"

S = "${WORKDIR}/u-boot-${PV}"
