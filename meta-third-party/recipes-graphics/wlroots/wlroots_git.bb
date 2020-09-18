DESCRIPTION = "Pluggable, composable, unopinionated modules for building a Wayland compositor; or about 50,000 lines of code you were going to write anyway."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7578fad101710ea2d289ff5411f1b818"

DEPENDS += "libinput libxkbcommon pixman virtual/egl wayland wayland-native wayland-protocols"

inherit meson

SRC_URI = " \
    git://github.com/swaywm/wlroots;protocol=https \
    file://0001-don-t-check-for-graphics-mode.patch \
"
SRCREV = "8ad2cc39eb420c22dde7e49c01bde916b7bc58cc"
S = "${WORKDIR}/git"

