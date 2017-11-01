require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "canvu500"

UBOOT_LOCALVERSION = "-venus"

SRC_URI = "\
	https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz \
"
SRC_URI[md5sum] = "33a9acc72dd3e24a038676b01b344ec3"
SRC_URI[sha256sum] = "eac51dd2b36689643737c0541086fda9f5871074b791fb135bbf4fae6240fda2"

S = "${WORKDIR}/u-boot-${PV}"
