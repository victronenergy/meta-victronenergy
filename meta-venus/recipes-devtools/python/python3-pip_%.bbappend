FILESEXTRAPATHS:prepend := "${THISDIR}/python3-pip:"

SRC_URI += " \
    file://0001-Remove-emsign-437.patch;patchdir=src/pip/_vendor \
    file://0002-Restore-unaffected-emSign-roots-439-440.patch;patchdir=src/pip/_vendor \
"

