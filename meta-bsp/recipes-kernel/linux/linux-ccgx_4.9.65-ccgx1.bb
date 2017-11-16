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

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

SRC_URI = " \
	git://github.com/victronenergy/linux;branch=ccgx-4.9 \
"
SRCREV = "2387bf5ce4c356f333dadcab2f3adceeba55dbd1"
