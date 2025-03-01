FILESEXTRAPATHS:prepend := "${THISDIR}:"

SRC_URI += "\
    file://haveged.rules \
"

do_install:append() {
    install -D -m 644 ${UNPACKDIR}/haveged.rules ${D}${sysconfdir}/udev/rules.d/55-haveged.rules
}
