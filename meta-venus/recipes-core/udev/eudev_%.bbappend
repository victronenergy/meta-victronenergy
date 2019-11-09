FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://set-base-compatible.patch;patchdir=${WORKDIR} \
"
