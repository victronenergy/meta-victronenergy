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
SRC_URI[md5sum] = "3a06cb159fe86c379ec02bae91bdcb78"
SRC_URI[sha256sum] = "92576a8a89a3e7d2a913278fb74d3ce306e2fbb78d8eff9dd080d7b9f4b8048f"
