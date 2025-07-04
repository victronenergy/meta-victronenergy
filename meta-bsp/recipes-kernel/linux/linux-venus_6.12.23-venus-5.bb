SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

SRC_URI = " \
    git://github.com/victronenergy/linux.git;protocol=https;nobranch=1 \
    file://0001-ARM-dts-bbb-venus-disable-dma-on-uart2.patch \
"
SRCREV = "a334bd8b1acab04eb85359b4c8a7735a6d60715b"
S = "${WORKDIR}/git"
UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"

RDEPENDS:${KERNEL_PACKAGE_NAME}-base += "kernel-devicetree"
RDEPENDS:${KERNEL_PACKAGE_NAME}-base:ccgx = ""

KERNEL_EXTRA_ARGS:ccgx = "zImage"
KEEPUIMAGE:ccgx = "no"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KERNEL_CONFIG}"

# To be compatible with the deployed u-boot, a uImage containing a
# zImage with dtb appended is created.
uboot_prep_kimage:prepend:ccgx() {
    cat arch/${ARCH}/boot/zImage \
        arch/${ARCH}/boot/dts/${KERNEL_DEVICETREE} \
        > linux.bin
    linux_comp=none
    return
}
