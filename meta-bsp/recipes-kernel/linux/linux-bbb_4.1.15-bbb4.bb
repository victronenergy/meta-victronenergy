SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

RDEPENDS_kernel-base += "kernel-devicetree"

KERNEL_PRIORITY = "0"
LINUX_KERNEL_TYPE = "standard"
KERNEL_CONFIG_COMMAND = "make -C ${S} O=${B} ARCH=arm bbb_defconfig"

KERNEL_DEVICETREE = " \
    am335x-boneblack.dtb \
    bbb-venus.dtb \
"

LINUX_VERSION = "4.1"
LINUX_VERSION_EXTENSION = "-venus"

inherit kernel
require recipes-kernel/linux/linux-dtb.inc

S = "${WORKDIR}/linux-${PV}"
B = "${WORKDIR}/linux-${PACKAGE_ARCH}-${LINUX_KERNEL_TYPE}-build"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "efe36896c7582ea0e71bbc0b0a861e68"
SRC_URI[sha256sum] = "3a7c28ab274247b3e5e07f9f108828a8f3273b9b55334626d1d9ee53bcb00f77"
