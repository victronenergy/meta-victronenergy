SUMMARY = "Socketcand, socketcan over tcp/ip"
LICENSE = "GPL-2.0-only | BSD-3-Clause"
LIC_FILES_CHKSUM = " \
    file://LICENSES/BSD-3-Clause;md5=4c00cf8b0a04a9441d8fa24850231d00 \
    file://LICENSES/GPL-2.0-only.txt;md5=f9d20a453221a1b7e32ae84694da2c37 \
"

SRC_URI = "git://github.com/linux-can/socketcand;branch=master;protocol=https"
SRC_URI += "file://always-use-64-bit-for-timevals.patch"
SRCREV = "30e584a5e0cc82670c11632357b6dccdf64ca98e"
S = "${WORKDIR}/git"

inherit meson pkgconfig

PACKAGECONFIG ?= "libconfig libsocketcan"
PACKAGECONFIG[libconfig] = "-Dlibconfig=true,-Dlibconfig=false,libconfig"
PACKAGECONFIG[libsocketcan] = "-Dlibsocketcan=true,-Dlibsocketcan=false,libsocketcan"
