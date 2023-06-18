SUMMARY = "Add the rtl8723bu driver from Larry Finger as an out-of-tree module"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
    gitsm://github.com/lwfinger/rtl8723bu.git;branch=master;protocol=https \
    file://0001-disable-proc-debug.patch \
    file://0001-disable-roaming.patch \
    file://be-less-verbose.patch \
"
SRCREV = "92c19318cb54ef96c2cfb4a22b2c98eb512812d8"

S = "${WORKDIR}/git"

inherit module

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.

EXTRA_OEMAKE += "KSRC=${STAGING_KERNEL_DIR}"

do_install() {
    # Module
    dest=${D}/lib/modules/${KERNEL_VERSION}/${PN}
    install -d ${dest}
    install -m 0644 8723bu.ko ${dest}
}
