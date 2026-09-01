FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://set-base-compatible.patch;patchdir=${UNPACKDIR} \
"

do_install:append:raspberrypi5() {
    sed -i 's/^[[:space:]]*udevadm settle$/            udevadm settle --timeout=15 || true/' \
        ${D}${sysconfdir}/init.d/udev
}
