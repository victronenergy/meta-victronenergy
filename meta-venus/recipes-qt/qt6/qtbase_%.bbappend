FILESEXTRAPATHS:prepend := "${THISDIR}/qtbase:"

PACKAGECONFIG:append:class-target = " kms"
PACKAGECONFIG:remove:class-target = "libinput"

SRC_URI += "file://0001-don-t-translate-coordinates-if-the-touch-coordinate-.patch"

RDEPENDS:${PN}:class-target += " \
    noto-sans-hinted-regular \
    noto-sans-hinted-regularitalic \
    noto-sans-hinted-bold \
    noto-sans-hinted-bolditalic \
    qtplatform \
"
