DESCRIPTION = "FlashMQ is a fast light-weight MQTT broker/server, designed to take good advantage of multi-CPU environments"

LICENSE = "OSL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=bec396627de11f42fe63f6d500f58d7a"
DEPENDS = "openssl"

inherit cmake

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "git://github.com/halfgaar/FlashMQ.git;branch=master;protocol=https"
SRCREV = "5b846dc3dcc176346a4728978293c71e76d07a2e"

S = "${WORKDIR}/git"
EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=Release"
OECMAKE_CXX_FLAGS += "-Wno-psabi"

# remove systemd service file
do_install:append() {
	rm -rf ${D}/lib
	rm -rf ${D}/var/log
}
