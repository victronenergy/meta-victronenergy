SUMMARY = "A hw-accelerated, touch only UI for Venus devices."

include gui-v2.inc

inherit daemontools qt6-cmake start-gui ve_package

# the .rcc files contain references to buildir
WARN_QA:remove = "buildpaths"
ERROR_QA:remove = "buildpaths"

DEPENDS += "qtdeclarative-native qttools-native"
DEPENDS += "qt5compat qtbase qtdeclarative qtmqtt qtshadertools qtsvg qtvirtualkeyboard qtwebchannel qtwebengine"
RDEPENDS:${PN} = " \
    qt5compat-qmlplugins \
    qtbase-plugin-qeglfs \
    qtbase-plugin-qeglfs-kms-integration \
    qtbase-plugin-qgif \
    qtbase-plugin-qlinuxfb \
    qtdeclarative-qmlplugins \
    qtsvg-plugin-qsvg \
    qtvirtualkeyboard-qmlplugins \
    qtwebchannel-modules \
    qtwebchannel-qmlplugins \
    qtwebengine \
    qtwebengine-modules \
    qtwebengine-qmlplugins \
    venus-ui-themes \
"
# FIXME: should become an RDEPEND of qtvirtualkeyboard-qmlplugins
RDEPENDS:${PN} += "qtvirtualkeyboard-plugin-qtvirtualkeyboardplugin"

PACKAGES += "start-gui-v2"
DAEMON_PN = "start-gui-v2"
RDEPENDS:${DAEMON_PN} = "${PN}"

DAEMONTOOLS_SCRIPT = ". /etc/profile.d/qt6.sh && export QTWEBENGINE_DISABLE_SANDBOX=1 && exec ${@softlimit(d, data=768000000, stack=1000000, all=768000000)} ${bindir}/venus-gui-v2"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/nmbath/gui-v2.git;branch=mbath/special;protocol=ssh;user=git \
    file://0001-cmake-use-CMAKE_CROSSCOMPILING-for-desktop-build-de.patch \
"
SRCREV = "f6f26c39603c4e170bebeac57878ff86852124fa"
S = "${WORKDIR}/git"

do_install:append() {
    # Ensure the cleanup succeeds even when cross-compiling on an aarch64 host machine.
    # On aarch64 build hosts, CMake may skip creating the target /usr directory structure, 
    # causing a standard 'rm' command to fail with a "No such file or directory" error.
    rm -rf ${D}/usr
}
