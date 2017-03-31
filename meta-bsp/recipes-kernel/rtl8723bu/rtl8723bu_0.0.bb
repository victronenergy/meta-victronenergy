SUMMARY = "Add the rtl8723bu driver from Larry Finger as an out-of-tree module"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

# When building on openembedded Jethro, or older versions, make sure to apply
# this commit:
# https://github.com/openembedded/openembedded-core/commit/afcea61e8eb39234d336c706fdfd4680dea7c060
# to prevent warnings exactly like mentioned in that commit message.

inherit module

SRC_URI = " \
	gitsm://github.com/lwfinger/rtl8723bu.git;protocol=https;rev=3ad44615955998368c1018f51719373275706cc6 \
	file://0001-fix-makefile-for-openembedded.patch \
	file://0001-disable-proc-debug.patch \
"

S = "${WORKDIR}/git"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.

do_install() {
    # Module
    install -d ${D}/lib/modules/${KERNEL_VERSION}/kernel/net/wireless
    install -m 0644 8723bu.ko ${D}/lib/modules/${KERNEL_VERSION}/kernel/net/wireless/8723bu.ko
}
