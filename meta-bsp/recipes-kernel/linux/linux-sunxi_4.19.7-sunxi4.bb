SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel

COMPATIBLE_MACHINE = "sunxi"

RDEPENDS_kernel-base += "kernel-devicetree"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} sunxi_victron_defconfig"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "7448003f006a7b82c4bfe711d30b0726"
SRC_URI[sha256sum] = "d99d67641899c7152b93df7da32dae3dffbd87ba145ecb101d8eb55d059bdf94"

S = "${WORKDIR}/linux-${PV}"

# fix make[3]: *** [scripts/extract-cert] Error 1
DEPENDS += "openssl-native"
HOST_EXTRACFLAGS += "-I${STAGING_INCDIR_NATIVE}"
