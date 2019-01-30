FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://blocking_getrandom.patch"
