FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-omit-variant-from-archive-name.patch \
    file://0002-add-stop-signal-option.patch \
"
