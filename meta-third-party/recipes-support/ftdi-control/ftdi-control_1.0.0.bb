SUMMARY = "Basic control and eeprom routines for FTDI FTx232 chips."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=40b6d360639a1603e7be0a38631d5a3d"

inherit autotools-brokensep pkgconfig

DEPENDS = "libftdi"

SRC_URI = " \
	git://github.com/aehparta/ftdi-bitbang.git \
	file://0001-use-LDADD-LIBADD-instead-of-LDFLAGS-for-the-librarie.patch \
	file://0002-print-the-usbid-of-the-usb-device.patch \
	file://0003-prevent-leaking-memory.patch \
	file://0004-add-an-option-to-select-on-usb-id.patch \
"
SRCREV = "0f4daf1e70ab0048e04ea0dfb7fba7aa72a9f861"
S = "${WORKDIR}/git"

do_configure_prepend () {
	if ! ./autogen.sh; then
		echo "Failed to run autogen.sh!"
		exit 1
	fi
}
