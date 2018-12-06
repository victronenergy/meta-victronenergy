SUMMARY = "RTL8192AU kernel driver (wifi)"
DESCRIPTION = "RTL8192AU kernel driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://hal/hal_com_c2h.h;md5=1b3bc120406d289b6d969a5dd22cac87;endline=19"

SRC_URI = " \
	git://github.com/Mange/rtl8192eu-linux-driver.git;protocol=https;branch=realtek-4.4.x \
	file://0001-ieee80211-use-static-inline.patch \
"
SRCREV = "63154169085566f879085678b5447bae12f8d69c"

S = "${WORKDIR}/git"
EXTRA_OEMAKE += "KSRC=${STAGING_KERNEL_DIR}"

inherit module

do_install () {
    install -d ${D}/lib/modules/${KERNEL_VERSION}
    install -m 0755 ${B}/8192eu.ko ${D}/lib/modules/${KERNEL_VERSION}/8192eu.ko
}

