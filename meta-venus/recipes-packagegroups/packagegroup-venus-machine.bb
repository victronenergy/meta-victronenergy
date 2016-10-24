SUMMARY = "common machine specific venus base packages"
DESCRIPTION = "see packagegroup-venus-base, this contain the per MACHINE base adjustments."

PR = "r0"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup
LICENSE = "MIT"


DEPENDS_append_ccgx += "\
	qtbase \
	qtserialport \
"

DEPENDS_append_bpp3 += "\
	qtbase \
	qtserialport \
"

RDEPENDS_${PN} += "\
	simple-upnpd \
"

RDEPENDS_${PN}_append_ccgx += "\
	mtd-utils \
	mtd-utils-ubifs \
	prodtest \
	swupdate \
	swupdate-scripts \
"

RDEPENDS_${PN}_append_bpp3 += "\
	mtd-utils \
	mtd-utils-ubifs \
"

RDEPENDS_${PN}_append_beaglebone += "\
	gpio-export \
	i2c-tools \
	swupdate \
	swupdate-scripts \
"
