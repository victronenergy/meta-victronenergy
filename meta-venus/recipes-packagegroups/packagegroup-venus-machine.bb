SUMMARY = "common machine specific venus base packages"
DESCRIPTION = "see packagegroup-venus-base, this contain the per MACHINE base adjustments."

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup
LICENSE = "MIT"

RDEPENDS_${PN} += "\
    simple-upnpd \
"

QT5_RDEPENDS = " \
    cinematicexperience \
    eglinfo-fb \
    qt5-opengles2-test \
    qtconnectivity \
    qtplatform \
    qtquickcontrols \
    qtquickcontrols2 \
    qtserialport \
    qtsvg-plugins \
"

RDEPENDS_${PN}_append_canvu500 += "\
    crda \
    evtest \
    gpio-export \
    imx-kobs \
    kmscube \
    mtd-utils \
    mtd-utils-ubifs \
    prodtest \
    swupdate \
    swupdate-scripts \
    ${QT5_RDEPENDS} \
"

RDEPENDS_${PN}_append_ccgx += "\
    gpio-export \
    mtd-utils \
    mtd-utils-ubifs \
    prodtest \
    swupdate \
    swupdate-scripts \
    technexion-serial \
    wireless-regdb-static \
"

RDEPENDS_${PN}_append_beaglebone += "\
    crda \
    gpio-export \
    hostapd \
    i2c-tools \
    linux-firmware-rtl-bt \
    prodtest \
    rtl8723bu \
    rtl8723du \
    swupdate \
    swupdate-scripts \
    venus-button-handler \
"

RDEPENDS_${PN}_append_einstein += "\
    linux-firmware-bcm20702a1 \
    linux-firmware-bcm43362 \
    parse-edid \
    sunxi-mali \
    sunxi-mali-blobs \
    venus-button-handler \
"

RDEPENDS_${PN}_append_sunxi += "\
    cpufrequtils \
    gpio-export \
    hostapd \
    linux-firmware-rtl-bt \
    prodtest \
    rtl8723bu \
    rtl8723du \
    swupdate \
    swupdate-scripts \
    wireless-regdb-static \
"

RDEPENDS_${PN}_append_raspberrypi2 += "\
    crda \
    gpio-export \
    linux-firmware-bcm43430 \
    linux-firmware-bcm43455 \
    swupdate \
    swupdate-scripts \
"
