SUMMARY = "Driver for RTL8723DU wifi module"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
    git://github.com/lwfinger/rtl8723du.git;protocol=https;branch=master \
"
SRCREV = "a8735e5e739a1e01aa17a6bd34b5cedba992b0be"

S = "${WORKDIR}/git"

inherit module

DEPENDS += "bc-native"

EXTRA_OEMAKE += "KSRC=${STAGING_KERNEL_DIR}"
EXTRA_OEMAKE += "USER_EXTRA_CFLAGS=-DCONFIG_CONCURRENT_MODE"

do_install() {
    dest=${D}/lib/modules/${KERNEL_VERSION}/${PN}
    install -d ${dest}
    install -m 0644 8723du.ko ${dest}
}
