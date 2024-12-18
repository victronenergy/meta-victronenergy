DESCRIPTION = "This is a liberally licensed VNC server library that's intended to be fast and neat."
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=94fc374e7174f41e3afe0f027ee59ff7"

DEPENDS += "aml libdrm libjpeg-turbo pixman"

inherit meson pkgconfig

SRC_URI = "git://github.com/any1/neatvnc;protocol=https;branch=v0.8"
SRCREV = "07081567ab21a2b099ceb41ae8cab872a31cbb9a"
S = "${WORKDIR}/git"

