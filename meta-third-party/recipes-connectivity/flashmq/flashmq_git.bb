DESCRIPTION = "FlashMQ is a fast light-weight MQTT broker/server, designed to take good advantage of multi-CPU environments"

LICENSE = "AGPL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb1e647870add0502f8f010b19de32af"
DEPENDS = "openssl"

inherit cmake

SRC_URI = "git://github.com/halfgaar/FlashMQ.git;protocol=https"
SRCREV = "57e6f5a4d511554b310dfadee2e4c62f7e00bfbb"
S = "${WORKDIR}/git"
EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=Release"
OECMAKE_CXX_FLAGS += "-Wno-psabi"

# remove systemd service file
do_install:append() {
	rm -rf ${D}/lib
	rm -rf ${D}/var/log
}

