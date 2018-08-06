SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

COMPATIBLE_MACHINE = "ccgx"

inherit kernel

LINUX_VERSION_EXTENSION = "-venus"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} am3517_ccgx_defconfig"
KERNEL_IMAGETYPES = "uImage"
KERNEL_EXTRA_ARGS = "zImage"
#KERNEL_DEVICETREE = "am3517-ccgx.dtb"
KEEPUIMAGE = "no"
UBOOT_ENTRYPOINT = "80008000"

# To be compatible with the deployed u-boot, a uImage containing a
# zImage with dtb appended is created.
#uboot_prep_kimage() {
#	cat arch/${ARCH}/boot/zImage arch/${ARCH}/boot/dts/am3517-ccgx.dtb \
#		> linux.bin
#        linux_comp=none
#}

RDEPENDS_kernel-base = "linux-backports"

S = "${WORKDIR}/linux-${PV}"
B = "${WORKDIR}/build"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "8fe90da914c4748cc6b389a15bf9623e"
SRC_URI[sha256sum] = "74053acb928380c3677c6b704eb823db9338a042cd7a150056df9e7cde7838a6"
