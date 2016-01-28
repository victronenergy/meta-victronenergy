FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# note: the busybox wget version was used, but could hang. It is replaced by the full
# wget. To make sure there is always a wget installed (opkg depends on it), wget is now
# a dependencies of busybox.
RDEPENDS_${PN} = "wget"

SRC_URI += "file://0001-disable-suppressing-0x7F-chars-in-printable-string.patch"

PR = "r16"
