FILESEXTRAPATHS:prepend := "${THISDIR}/qtbase:"

PACKAGECONFIG:append:class-target = " gbm kms linuxfb"
PACKAGECONFIG:remove:class-target = "harfbuzz icu libinput"

SRC_URI += "file://0001-don-t-translate-coordinates-if-the-touch-coordinate-.patch"

# Qt will enable the stack protection when the compiler reports that it is
# supported. For arm and aarch64 the compiler does report it is supported,
# but its implementation causes problems e.g. it causes valgrind to crash
# and report invalid issues.
#
# Since the issues is known for a long time and marked as "won't fix"
# see  https://bugzilla.redhat.com/show_bug.cgi?id=1522678, disable it
# for ARM. It has also been reported to valgrind itself:
# https://bugs.kde.org/show_bug.cgi?id=479996
# https://bugs.kde.org/show_bug.cgi?id=479997

EXTRA_OECMAKE_DISABLE_STACK_PROTECTOR += " \
    -DFEATURE_stack_protector=OFF \
    -DFEATURE_stack_clash_protection=OFF \
"
EXTRA_OECMAKE:append:arm = " ${EXTRA_OECMAKE_DISABLE_STACK_PROTECTOR}"
EXTRA_OECMAKE:append:aarch64 = " ${EXTRA_OECMAKE_DISABLE_STACK_PROTECTOR}"

# ommitted qtplatform as RDEPEND, since it is machine dependent
