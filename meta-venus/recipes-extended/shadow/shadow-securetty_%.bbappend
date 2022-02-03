FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://securetty-add-serial-ports.patch;patchdir=${WORKDIR} \
"
