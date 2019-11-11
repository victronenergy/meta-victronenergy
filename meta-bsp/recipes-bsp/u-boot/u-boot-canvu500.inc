require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-canvu500:"

COMPATIBLE_MACHINE = "canvu500"

UBOOT_LOCALVERSION = "-venus"

SRC_URI = "\
    https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz \
"
SRC_URI[md5sum] = "cb22b1a1e62d8555665b40bc4658fd62"
SRC_URI[sha256sum] = "0dc1ef04b643e6b5f8177b91cb850f409dbcb463f6b7c446fb43f5716d7d1b25"

S = "${WORKDIR}/u-boot-${PV}"
