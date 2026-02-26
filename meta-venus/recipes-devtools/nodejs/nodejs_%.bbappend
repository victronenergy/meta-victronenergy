FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += " \
    file://0001-src-fix-pointer-alignment.patch \
    file://0001-fix-handle-match_mask-0-in-NEON-ctzll-to-avoid-undef.patch \
"

