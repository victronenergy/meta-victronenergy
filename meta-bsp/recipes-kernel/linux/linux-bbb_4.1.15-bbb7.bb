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
SRC_URI[md5sum] = "a2684e5c697d5028a9a8e3395d9f01c0"
SRC_URI[sha256sum] = "07e9e8df21c2a3c0cef6dcf74499e44c6236a51ad56602bad5634a90364526df"
