SUMMARY = "Add the rtl8723bu driver from Larry Finger as an out-of-tree module"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
    gitsm://github.com/lwfinger/rtl8723bu.git;protocol=https;rev=b5bad435151c7fa6fa1ef9c1923b6d7679df2430 \
    file://0001-fix-makefile-for-openembedded.patch \
    file://0001-disable-proc-debug.patch \
    file://0001-disable-roaming.patch \
    file://be-less-verbose.patch \
"

S = "${WORKDIR}/git"

inherit module

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.

do_install() {
    # Module
    dest=${D}/lib/modules/${KERNEL_VERSION}/${PN}
    install -d ${dest}
    install -m 0644 8723bu.ko ${dest}
}
