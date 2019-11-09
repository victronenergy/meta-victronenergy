SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel

COMPATIBLE_MACHINE = "sunxi"

RDEPENDS_kernel-base += "kernel-devicetree"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} sunxi_victron_defconfig"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz \
    file://0001-configs-sunxi_victron-disable-mousedev.patch "
SRC_URI[md5sum] = "fb9458776cfc1087e739c9f7bdf34318"
SRC_URI[sha256sum] = "4b159955b429f997fb6283d2201e596095f604e5c42e4dedb3f8807f32fbb668"

S = "${WORKDIR}/linux-${PV}"

# fix make[3]: *** [scripts/extract-cert] Error 1
DEPENDS += "openssl-native"
HOST_EXTRACFLAGS += "-I${STAGING_INCDIR_NATIVE}"
