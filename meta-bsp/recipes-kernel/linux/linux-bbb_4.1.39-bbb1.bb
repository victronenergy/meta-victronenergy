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
SRC_URI[md5sum] = "02a6a13d4ce62da57d12d8b40b593e2f"
SRC_URI[sha256sum] = "fce07c38f143ba4044716427f80e07f3de5ec681de65c4193a0f0f0e84f71404"

SRC_URI += " \
	file://0001-Bluetooth-btusb-fix-Realtek-suspend-resume.patch \
	file://0002-Bluetooth-btusb-match-generic-class-code-in-interfac.patch \
	file://0003-bbb_defconfig-enable-bluetooth.patch \
"
