FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# see https://bugs.debian.org/cgi-bin/bugreport.cgi?bug=897943 and
# https://security-tracker.debian.org/tracker/CVE-2018-1108 and
# https://www.spinics.net/lists/util-linux-ng/msg15176.html

# Both sfdisk and mk2fs.ext4 wait till crng is seeded which takes a long
# time when CVE-2018-1108 is fixed: [185.531378] random: crng init done.
# This is fixed in util-linux v2.32. This backports all changes of randutils
# from that version to v2.30, so this delay, mostly notable in install images,
# is no longer there.

SRC_URI += " \
    file://0001-lib-randutils.c-Fall-back-gracefully-when-kernel-doe.patch \
    file://0002-lib-randutils.c-More-paranoia-in-getrandom-call.patch \
    file://0003-lib-randutils-improve-getrandom-usage.patch \
    file://0004-lib-randutils-reset-lose-counter.patch \
    file://0005-misc-fix-some-printf-format-strings.patch \
    file://0006-lib-randutils-remove-superfluous-continue.patch \
    file://0007-lib-randutils-Do-not-block-on-getrandom.patch \
    file://0008-lib-randutils-don-t-break-on-EAGAIN-use-usleep.patch \
"
