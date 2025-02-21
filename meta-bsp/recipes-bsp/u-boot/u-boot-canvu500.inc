require u-boot.inc

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
FILESEXTRAPATHS:prepend := "${THISDIR}/u-boot-canvu500:"

COMPATIBLE_MACHINE = "canvu500"

SRC_URI = "\
    https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz \
"
SRC_URI[sha256sum] = "0dc1ef04b643e6b5f8177b91cb850f409dbcb463f6b7c446fb43f5716d7d1b25"
INSANE_SKIP += "src-uri-bad"

S = "${WORKDIR}/u-boot-${PV}"
