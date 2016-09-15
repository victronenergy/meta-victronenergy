SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

RDEPENDS_kernel-base += "kernel-devicetree rtl8723bu"

KERNEL_PRIORITY = "0"
LINUX_KERNEL_TYPE = "standard"
KERNEL_CONFIG_COMMAND = "make -C ${S} O=${B} ARCH=arm bbb_defconfig"

KERNEL_DEVICETREE = " \
    am335x-boneblack.dtb \
    bbb-venus.dtb \
    bbe-venus.dtb \
"

LINUX_VERSION = "4.1"
LINUX_VERSION_EXTENSION = "-venus"

inherit kernel
require recipes-kernel/linux/linux-dtb.inc

S = "${WORKDIR}/linux-${PV}"
B = "${WORKDIR}/linux-${PACKAGE_ARCH}-${LINUX_KERNEL_TYPE}-build"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "f45cfc88490a983ccdae362b16e18b33"
SRC_URI[sha256sum] = "23425a51e93811f9470ef0781e38c10b005aef61dd6967afbc7affc03cc3387e"
