SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel

COMPATIBLE_MACHINE = "sunxi"

RDEPENDS_kernel-base += "kernel-devicetree"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} sunxi_victron_defconfig"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "2746d5b6dd9024963f6ce5128e8eab9b"
SRC_URI[sha256sum] = "16b30abbfd4e8cb6d6e04a9d929853f449c64690465de72c6eca581bc833d9b0"

S = "${WORKDIR}/linux-${PV}"

# fix make[3]: *** [scripts/extract-cert] Error 1
DEPENDS += "openssl-native"
HOST_EXTRACFLAGS += "-I${STAGING_INCDIR_NATIVE}"
