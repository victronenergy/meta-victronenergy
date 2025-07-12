SUMMARY = "Basic control and eeprom routines for FTDI FTx232 chips."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=40b6d360639a1603e7be0a38631d5a3d"

inherit autotools-brokensep pkgconfig

DEPENDS = "libftdi"

SRC_URI = "git://github.com/aehparta/ftdi-bitbang.git;branch=master;protocol=https"
SRCREV = "cf11d84b46946e2e99561083795e6336261affb7"
S = "${WORKDIR}/git"

do_configure:prepend () {
    if ! ./autogen.sh; then
        echo "Failed to run autogen.sh!"
        exit 1
    fi
}
