SUMMARY = "Basic control and eeprom routines for FTDI FTx232 chips."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3da9cfbcb788c80a0384361b4de20420"

inherit autotools-brokensep pkgconfig

DEPENDS = "libftdi"

SRC_URI = " \
	git://github.com/aehparta/ftdi-bitbang.git \
	file://0001-use-LDADD-LIBADD-instead-of-LDFLAGS-for-the-librarie.patch \
"
SRCREV = "0f4daf1e70ab0048e04ea0dfb7fba7aa72a9f861"
S = "${WORKDIR}/git"

do_configure_prepend () {
	if ! ./autogen.sh; then
		echo "Failed to run autogen.sh!"
		exit 1
	fi
}
