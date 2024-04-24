require linux-venus.bb

COMPATIBLE_MACHINE = "ccgx"

KERNEL_EXTRA_ARGS = "zImage"
KEEPUIMAGE = "no"

# To be compatible with the deployed u-boot, a uImage containing a
# zImage with dtb appended is created.
uboot_prep_kimage:prepend() {
    cat arch/${ARCH}/boot/zImage \
        arch/${ARCH}/boot/dts/${KERNEL_DEVICETREE} \
        > linux.bin
    linux_comp=none
    return
}

RDEPENDS:${KERNEL_PACKAGE_NAME}-base = ""
