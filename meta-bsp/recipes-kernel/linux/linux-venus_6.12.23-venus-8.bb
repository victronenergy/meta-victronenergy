SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

# Auto generated CVE exclusion from a db.
# See sources/openembedded-core/meta/recipes-kernel/linux/generate-cve-exclusions.py
LINUX_VERSION = "${@d.getVar("PV").split("-")[0] }"
include linux-venus-cve-exclusion-generated.inc

# manual known cve exclusion by OE
include recipes-kernel/linux/cve-exclusion.inc

# still unknown cve statuses
include linux-venus-cve-exclusion.inc

SRC_URI = "git://github.com/victronenergy/linux.git;protocol=https;nobranch=1"
BB_GIT_DEFAULT_DESTSUFFIX ?= "git"
S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}"

SRCREV = "00fa55f95948efef92cbf3cbd2daf997255530fc"
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
