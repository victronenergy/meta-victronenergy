DESCRIPTION = "This is a liberally licensed VNC server library that's intended to be fast and neat."
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=94fc374e7174f41e3afe0f027ee59ff7"

DEPENDS += "aml libdrm libjpeg-turbo pixman virtual/libgbm"

inherit meson

SRC_URI = "git://github.com/any1/neatvnc;protocol=https"
SRCREV = "b932f3e2e089d85dce0cfe4cf97041bc573590aa"
SRC_URI += "file://null-dereference.patch"
S = "${WORKDIR}/git/"

