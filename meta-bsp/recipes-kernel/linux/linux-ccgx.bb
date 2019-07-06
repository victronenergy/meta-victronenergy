require linux-venus.bb

COMPATIBLE_MACHINE = "ccgx"

KERNEL_CONFIG = "ccgx_defconfig"
KERNEL_EXTRA_ARGS = "zImage"
KERNEL_DEVICETREE = "am3517-ccgx.dtb"
KEEPUIMAGE = "no"

# To be compatible with the deployed u-boot, a uImage containing a
# zImage with dtb appended is created.
uboot_prep_kimage() {
    cat arch/${ARCH}/boot/zImage \
        arch/${ARCH}/boot/dts/${KERNEL_DEVICETREE} \
        > linux.bin
    linux_comp=none
}

RDEPENDS_${KERNEL_PACKAGE_NAME}-base = ""
