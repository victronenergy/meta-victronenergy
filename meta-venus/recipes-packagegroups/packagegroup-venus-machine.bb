SUMMARY = "common machine specific venus base packages"
DESCRIPTION = "see packagegroup-venus-base, this contain the per MACHINE base adjustments."

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup
LICENSE = "MIT"

include qt6-packages.inc

# NOTE: some machine runtime dependency are added here, since adding them to the package
# themselves causes the task signatures to change and that propagates to all dependent
# tasks. E.g. qt4-machine-conf will cause the packaging of qt4 to become machine specific.
RDEPENDS:${PN} += "\
    machine-runtime-conf \
    qt4-machine-conf \
    qtplatform \
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
    vegetty \
"

RDEPENDS:${PN}:append:beaglebone = "\
    gpio-export \
    hostapd \
    i2c-tools \
    rtl8723bu \
    rtl8723du \
    swupdate \
    swupdate-scripts \
    venus-button-handler \
"

RDEPENDS:${PN}:append:einstein = "\
    atmel-adc-slcan \
    linux-firmware-bcm20702a1 \
    linux-firmware-bcm43362 \
    linux-firmware-bcm43430 \
    linux-firmware-bcm43430a1 \
    read-edid \
    venus-button-handler \
    venus-firmware-update \
"

RDEPENDS:${PN}:append:ekrano = "\
    edid-ekrano \
"

RDEPENDS:${PN}:append:nanopi = " \
    dbus-characterdisplay \
"

RDEPENDS:${PN}:append:sunxi = "\
    cpufrequtils \
    gpio-export \
    hostapd \
    rtl8723bu \
    rtl8723du \
    swupdate \
    swupdate-scripts \
"

RDEPENDS:${PN}:append:rpi = "\
    gpio-export \
    resize-sdcard \
    swupdate \
    swupdate-scripts \
    wireless-regdb-static \
"
