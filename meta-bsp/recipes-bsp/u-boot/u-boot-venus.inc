HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
DESCRIPTION = "U-Boot, a boot loader for Embedded boards based on PowerPC, \
ARM, MIPS and several other processors, which can be installed in a boot \
ROM and used to initialize and test the hardware or to download and run \
application code."
SECTION = "bootloaders"
DEPENDS += "flex-native bison-native"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/LGPL-2.1-or-later;md5=2a4f4fd2128ea2f65047ee63fbca9f68"

FILESEXTRAPATHS:prepend := "${THISDIR}/u-boot-venus:"
UBOOT_SRC_URI ?= ""
SRC_URI = "${UBOOT_SRC_URI}"
SRC_URI += "file://0001-rpi-switch-a-b-partitions-based-on-the-FAT-partition.patch"
INSANE_SKIP += "src-uri-bad"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"
