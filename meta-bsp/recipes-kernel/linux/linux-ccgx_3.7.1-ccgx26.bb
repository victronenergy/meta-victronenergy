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
SRC_URI[md5sum] = "6a3b15c58aace313196051809ae03a08"
SRC_URI[sha256sum] = "cfa1f82c30d312a1d772aaffc4b5c537c8dd8b41be2eacfdf4def079472094c4"
