require linux-venus.inc

SRC_URI = " \
    git://github.com/victronenergy/linux.git;protocol=https;nobranch=1 \
    file://0001-ARM-dts-bbb-venus-disable-dma-on-uart2.patch \
"
SRCREV = "a334bd8b1acab04eb85359b4c8a7735a6d60715b"
UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"

KERNEL_EXTRA_ARGS:ccgx = "zImage"
KEEPUIMAGE:ccgx = "no"

# To be compatible with the deployed u-boot, a uImage containing a
# zImage with dtb appended is created.
uboot_prep_kimage:prepend:ccgx() {
    cat arch/${ARCH}/boot/zImage \
        arch/${ARCH}/boot/dts/${KERNEL_DEVICETREE} \
        > linux.bin
    linux_comp=none
    return
}
