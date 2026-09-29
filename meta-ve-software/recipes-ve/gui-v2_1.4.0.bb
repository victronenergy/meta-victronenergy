SUMMARY = "A hw-accelerated, touch only UI for Venus devices."

include gui-v2.inc

inherit daemontools qt6-cmake start-gui ve_package

# the .rcc files contain references to buildir
WARN_QA:remove = "buildpaths"
ERROR_QA:remove = "buildpaths"

DEPENDS += "qtdeclarative-native qttools-native"
DEPENDS += "qt5compat qtbase qtdeclarative qtmqtt qtshadertools qtsvg qtvirtualkeyboard"
RDEPENDS:${PN} = " \
    qt5compat-qmlplugins \
    qtbase-plugin-qeglfs \
    qtbase-plugin-qeglfs-kms-integration \
    qtbase-plugin-qgif \
    qtbase-plugin-qlinuxfb \
    qtdeclarative-qmlplugins \
    qtsvg-plugin-qsvg \
    qtvirtualkeyboard-qmlplugins \
    venus-ui-themes \
"
# FIXME: should become an RDEPEND of qtvirtualkeyboard-qmlplugins
RDEPENDS:${PN} += "qtvirtualkeyboard-plugin-qtvirtualkeyboardplugin"

PACKAGES += "start-gui-v2"
DAEMON_PN = "start-gui-v2"
RDEPENDS:${DAEMON_PN} = "${PN}"

DAEMONTOOLS_SCRIPT = ". /etc/profile.d/qt6.sh && exec ${@softlimit(d, data=768000000, stack=1000000, all=768000000)} ${bindir}/venus-gui-v2"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/nmbath/gui-v2.git;branch=mbath/branding;protocol=ssh;user=git \
"
SRCREV = "4bd16b05d2a72447d21c41f43b32402273cf76e8"
S = "${WORKDIR}/git"

do_install:append() {
    # Ensure the cleanup succeeds even when cross-compiling on an aarch64 host machine.
    # On aarch64 build hosts, CMake may skip creating the target /usr directory structure, 
    # causing a standard 'rm' command to fail with a "No such file or directory" error.
    rm -rf ${D}/usr
}
