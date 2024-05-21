DESCRIPTION = "FlashMQ is a fast light-weight MQTT broker/server, designed to take good advantage of multi-CPU environments"

LICENSE = "OSL-3.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=bec396627de11f42fe63f6d500f58d7a"
DEPENDS = "openssl"

inherit cmake

SRC_URI = " \
    git://github.com/halfgaar/FlashMQ.git;branch=master;protocol=https;tag=v${PV} \
    file://0000-Fix-not-queueing-keep-alives.patch \
"

PR = "1"

S = "${WORKDIR}/git"
EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=Release"
OECMAKE_CXX_FLAGS += "-Wno-psabi"

# remove systemd service file
do_install:append() {
	rm -rf ${D}/lib
	rm -rf ${D}/var/log
}
