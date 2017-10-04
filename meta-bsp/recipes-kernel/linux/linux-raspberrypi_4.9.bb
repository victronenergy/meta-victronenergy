FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

LINUX_VERSION ?= "4.9.50"

SRCREV = "46e2d4d1bd2c17e2f84dd90768321ee0bbaa6b8a"
SRC_URI = "git://github.com/raspberrypi/linux.git;protocol=git;branch=rpi-4.9.y \
    file://logo.diff \
    file://logo.cfg \
    file://slcan.cfg \
    file://can-peak.cfg \
"
require linux-raspberrypi.inc
