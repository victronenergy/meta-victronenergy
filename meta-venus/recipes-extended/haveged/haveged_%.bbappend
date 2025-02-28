FILESEXTRAPATHS:prepend := "${THISDIR}:"

# The ccgx has problems getting initial random data, so help it a bit.
# Don't trouble other devices with it since that can actually cause
# more problems.

SRC_URI:append:ccgx = "\
    file://haveged.rules \
"

do_install:append:ccgx() {
    install -D -m 644 ${UNPACKDIR}/haveged.rules ${D}${sysconfdir}/udev/rules.d/55-haveged.rules
}
