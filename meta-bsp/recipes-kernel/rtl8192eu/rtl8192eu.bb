SUMMARY = "RTL8192AU kernel driver (wifi)"
DESCRIPTION = "RTL8192AU kernel driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://hal/hal_com_c2h.h;md5=d1415c223f48d77507154f01e7fa002f;endline=14"

SRC_URI = " \
    git://github.com/Mange/rtl8192eu-linux-driver.git;protocol=https;branch=realtek-4.4.x \
    file://0001-ieee80211-use-static-inline.patch \
"
SRCREV = "6d2705d8023c25b43f845298e707252fd813e68e"

S = "${WORKDIR}/git"
EXTRA_OEMAKE += "KSRC=${STAGING_KERNEL_DIR}"

inherit module

do_install () {
    dest=${D}/lib/modules/${KERNEL_VERSION}/${PN}
    install -d ${dest}
    install -m 0644 ${B}/8192eu.ko ${dest}
}

