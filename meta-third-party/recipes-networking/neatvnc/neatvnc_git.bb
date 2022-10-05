DESCRIPTION = "This is a liberally licensed VNC server library that's intended to be fast and neat."
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=94fc374e7174f41e3afe0f027ee59ff7"

DEPENDS += "aml libdrm libjpeg-turbo pixman"

inherit meson

SRC_URI = "git://github.com/any1/neatvnc;protocol=https"
SRCREV = "6ad4aba374153d36c99ea5073b747697774f4e3e"
S = "${WORKDIR}/git/"

