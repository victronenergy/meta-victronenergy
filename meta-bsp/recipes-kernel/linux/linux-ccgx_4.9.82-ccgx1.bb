SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

COMPATIBLE_MACHINE = "ccgx"

inherit kernel

DEPENDS += "u-boot-mkimage-native"

LINUX_VERSION_EXTENSION = "-venus"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ccgx_defconfig"
KERNEL_EXTRA_ARGS = "LOADADDR=80008000 am3517-ccgx.dtb"
KERNEL_IMAGETYPE = "uImage"

# To be compatible with the deployed u-boot, a uImage containing a
# zImage with dtb appended is created. This builds a regular zImage
# and dtb first, kernel_do_compile_append concatenates them and
# creates an uImage for it.
kernel_do_compile_append() {
	rm ${KERNEL_OUTPUT}
	cat arch/${ARCH}/boot/zImage arch/${ARCH}/boot/dts/am3517-ccgx.dtb > zImage-with-dtb
	mkimage -A arm -O linux -C none  -T kernel -a 80008000 -e 80008000 -n 'Linux-with-dtb' -d zImage-with-dtb ${KERNEL_OUTPUT}
}

S = "${WORKDIR}/linux-${PV}"
B = "${WORKDIR}/build"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "5da8f35fbcb555cef17571b59a56890a"
SRC_URI[sha256sum] = "f53f46360369c7a270fb1eb39e8435c8cf75284ef89dfeb5096ade8f70785ac3"
