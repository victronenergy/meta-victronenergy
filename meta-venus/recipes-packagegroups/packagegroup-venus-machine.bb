SUMMARY = "common machine specific venus base packages"
DESCRIPTION = "see packagegroup-venus-base, this contain the per MACHINE base adjustments."

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup
LICENSE = "MIT"

include qt6-packages.inc

# NOTE: some machine runtime dependency are added here, since adding them to the package
# themselves causes the task signatures to change and that propagates to all dependent
# tasks.
RDEPENDS:${PN} += "\
    machine-runtime-conf \
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
    haveged \
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
    venus-button-handler \
"

RDEPENDS:${PN}:append:rpi = "\
    bluez-firmware-rpidistro-bcm43430a1-hcd \
    bluez-firmware-rpidistro-bcm4345c0-hcd \
    bluez-firmware-rpidistro-bcm43430b0-hcd \
    bluez-firmware-rpidistro-bcm4345c5-hcd \
    gpio-export \
    linux-firmware-rpidistro-bcm43430 \
    linux-firmware-rpidistro-bcm43455 \
    linux-firmware-rpidistro-bcm43436s \
    linux-firmware-rpidistro-bcm43456 \
    linux-firmware-rpidistro-bcm43436 \
    resize-sdcard \
    swupdate \
    swupdate-scripts \
    wireless-regdb-static \
"

# netmon provides a workaround for an problem solved years ago
# and it is a open issue if the package shouldn't be dropped.
# Since it requires quite some resources with the scarthgap
# python version, drop it for the devices with little memory.
NETMON = " netmon"
NETMON:ccgx = ""
NETMON:canvu500 = ""
RDEPENDS:${PN}:append = "${NETMON}"

DBUS_SHELLY = " dbus-shelly"
DBUS_SHELLY:ccgx = ""
RDEPENDS:${PN}:append = "${DBUS_SHELLY}"
