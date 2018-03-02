FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

LINUX_VERSION ?= "4.9.80"

SRCREV = "027885087faa515133534cf767a1cd66cbd6cd1e"
SRC_URI = "git://github.com/raspberrypi/linux.git;protocol=git;branch=rpi-4.9.y \
    file://logo.patch \
    file://hjelmslund.patch \
    file://logo.cfg \
    file://slcan.cfg \
    file://can-peak.cfg \
"
require linux-raspberrypi.inc
