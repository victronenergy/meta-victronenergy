SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

RDEPENDS_kernel-base += "kernel-devicetree rtl8723bu"

KERNEL_CONFIG_COMMAND = "make -C ${S} O=${B} ARCH=arm bbb_defconfig"

KERNEL_DEVICETREE = " \
    am335x-boneblack.dtb \
    bbb-venus.dtb \
    bbb-octo-venus.dtb \
    bbe-venus.dtb \
"

LINUX_VERSION = "4.9"
LINUX_VERSION_EXTENSION = "-venus"

inherit kernel

S = "${WORKDIR}/linux-${PV}"
B = "${WORKDIR}/build"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "b73442c56a89d5daa3a1acf6b8c562bd"
SRC_URI[sha256sum] = "0d8da3a236cf75fa13f948e1b24d4443720c6d42080c0a6b62b7c72a0f6f470e"
