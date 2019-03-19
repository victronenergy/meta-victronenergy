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
	evtest \
	gpio-export \
	imx-kobs \
	kmscube \
	mtd-utils \
	mtd-utils-ubifs \
	pointercal \
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
"

RDEPENDS_${PN}_append_beaglebone += "\
	gpio-export \
	hostapd \
	i2c-tools \
	linux-firmware-rtl8723b \
	prodtest \
	swupdate \
	swupdate-scripts \
"

RDEPENDS_${PN}_append_einstein += "\
	linux-firmware-bcm20702a1 \
	linux-firmware-bcm43362 \
	sunxi-mali \
	sunxi-mali-blobs \
"

RDEPENDS_${PN}_append_sunxi += "\
	cpufrequtils \
	gpio-export \
	hostapd \
	linux-firmware-rtl8723b \
	prodtest \
	rtl8723bu \
	swupdate \
	swupdate-scripts \
"

RDEPENDS_${PN}_append_raspberrypi2 += "\
	gpio-export \
	linux-firmware-bcm43430 \
	linux-firmware-bcm43455 \
	swupdate \
	swupdate-scripts \
"
