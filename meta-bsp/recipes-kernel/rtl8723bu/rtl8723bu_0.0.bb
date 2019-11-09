SUMMARY = "Add the rtl8723bu driver from Larry Finger as an out-of-tree module"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = " \
    gitsm://github.com/lwfinger/rtl8723bu.git;protocol=https;rev=b5bad435151c7fa6fa1ef9c1923b6d7679df2430 \
    file://0001-fix-makefile-for-openembedded.patch \
    file://0001-disable-proc-debug.patch \
    file://0001-disable-roaming.patch \
"

S = "${WORKDIR}/git"

inherit module

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.

do_install() {
    # Module
    install -d ${D}/lib/modules/${KERNEL_VERSION}/kernel/net/wireless
    install -m 0644 8723bu.ko ${D}/lib/modules/${KERNEL_VERSION}/kernel/net/wireless/8723bu.ko
}
