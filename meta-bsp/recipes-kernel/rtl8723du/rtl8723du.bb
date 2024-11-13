SUMMARY = "Driver for RTL8723DU wifi module"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = " \
    git://github.com/lwfinger/rtl8723du.git;protocol=https;branch=v5.13.4 \
    file://${BPN}.conf \
"
SRCREV = "cfcb42ad6f9bc9bc987ef2f2841f15ce5fb4bbdb"

S = "${WORKDIR}/git"

inherit module

DEPENDS += "bc-native"

EXTRA_OEMAKE += "KSRC=${STAGING_KERNEL_DIR}"
EXTRA_OEMAKE += "USER_EXTRA_CFLAGS=-DCONFIG_CONCURRENT_MODE"
EXTRA_OEMAKE += "CONFIG_LAYER2_ROAMING=n"
EXTRA_OEMAKE += "CONFIG_RTW_DEBUG=n"

do_install() {
    dest=${D}/lib/modules/${KERNEL_VERSION}/${PN}
    install -d ${dest}
    install -m 0644 8723du.ko ${dest}

    dest=${D}${sysconfdir}/modprobe.d
    install -d ${dest}
    install -m 0644 ${WORKDIR}/${PN}.conf ${dest}
}

FILES:${PN} += "${sysconfdir}"
