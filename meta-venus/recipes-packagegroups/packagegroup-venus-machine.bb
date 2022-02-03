SUMMARY = "common machine specific venus base packages"
DESCRIPTION = "see packagegroup-venus-base, this contain the per MACHINE base adjustments."

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup
LICENSE = "MIT"

include qt6-packages.inc

RDEPENDS:${PN} += "\
    simple-upnpd \
"

RDEPENDS:${PN}:append:canvu500 = "\
    evtest \
    gpio-export \
    imx-kobs \
    kmscube \
    mtd-utils \
    mtd-utils-ubifs \
    swupdate \
    swupdate-scripts \
"

RDEPENDS:${PN}:append:ccgx = "\
    gpio-export \
    mtd-utils \
    mtd-utils-ubifs \
    swupdate \
    swupdate-scripts \
    technexion-serial \
"

RDEPENDS:${PN}:append:beaglebone = "\
    gpio-export \
    hostapd \
    i2c-tools \
    linux-firmware-rtl-bt \
    rtl8723bu \
    rtl8723du \
    swupdate \
    swupdate-scripts \
    venus-button-handler \
"

RDEPENDS:${PN}:append:einstein = "\
    linux-firmware-bcm20702a1 \
    linux-firmware-bcm43362 \
    linux-firmware-bcm43430 \
    linux-firmware-bcm43430a1 \
    read-edid \
    venus-button-handler \
"

RDEPENDS:${PN}:append:sunxi = "\
    cpufrequtils \
    gpio-export \
    hostapd \
    linux-firmware-rtl-bt \
    rtl8723bu \
    rtl8723du \
    swupdate \
    swupdate-scripts \
"

RDEPENDS:${PN}:append:rpi = "\
    bluez-firmware-rpidistro-bcm43430a1-hcd \
    bluez-firmware-rpidistro-bcm4345c0-hcd \
    gpio-export \
    linux-firmware-rpidistro-bcm43430 \
    linux-firmware-rpidistro-bcm43455 \
    resize-sdcard \
    swupdate \
    swupdate-scripts \
    wireless-regdb-static \
"
