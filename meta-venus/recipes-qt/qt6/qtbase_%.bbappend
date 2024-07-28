FILESEXTRAPATHS:prepend := "${THISDIR}/qtbase:"

PACKAGECONFIG:append:class-target = " gbm kms linuxfb"
PACKAGECONFIG:remove:class-target = "libinput"

SRC_URI += "file://0001-don-t-translate-coordinates-if-the-touch-coordinate-.patch"

# ommitted qtplatform as RDEPEND, since it is machine dependent
