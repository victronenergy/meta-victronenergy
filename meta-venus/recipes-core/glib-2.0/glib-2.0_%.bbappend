FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://0001-gfileutils-make-g_file_set_contents-always-fsync.patch \
    file://0002-flush-the-metadata-of-the-rename.patch \
"

