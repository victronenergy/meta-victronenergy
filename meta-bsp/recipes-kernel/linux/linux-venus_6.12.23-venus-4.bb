require linux-venus.inc

SRC_URI = " \
    git://github.com/victronenergy/linux.git;protocol=https;nobranch=1 \
    file://0001-ARM-dts-bbb-venus-disable-dma-on-uart2.patch \
"
SRCREV = "b6bd5a26f1c4555a0be418afa31ef2b83166153e"
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
