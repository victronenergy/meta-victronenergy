FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://set-base-compatible.patch;patchdir=${UNPACKDIR} \
    file://keep-existing-dev.patch;patchdir=${UNPACKDIR} \
"
