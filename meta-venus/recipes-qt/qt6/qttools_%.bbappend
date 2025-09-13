FILESEXTRAPATHS:prepend := "${THISDIR}/qttools:"
SRC_URI:append:class-target = " file://0001-qttools-disable-qhelpgenerator-fails-to-install.patch"

# for lupdate / lrelease on a target
QT_FORCE_BUILD_TOOLS:class-target = "1"

