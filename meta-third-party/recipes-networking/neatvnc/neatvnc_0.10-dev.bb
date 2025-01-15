DESCRIPTION = "This is a liberally licensed VNC server library that's intended to be fast and neat."
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=94fc374e7174f41e3afe0f027ee59ff7"

DEPENDS += "aml gmp libdrm libjpeg-turbo nettle pixman"

inherit meson pkgconfig

SRC_URI = "git://github.com/any1/neatvnc;protocol=https;branch=master"
SRC_URI += " \
	file://0001-Revert-Only-allow-a-curated-set-of-modifiers.patch \
	file://0001-tolower_and_remove_ws-always-increment-the-src-point.patch \
	file://0002-fix-the-Sec-WebSocket-Protocol.patch \
"
SRCREV = "8eba00a0285a2403a1b71619d6d47afa2f6466ac"
S = "${WORKDIR}/git"

