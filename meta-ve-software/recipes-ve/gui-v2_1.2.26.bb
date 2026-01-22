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
    gitsm://github.com/victronenergy/gui-v2.git;branch=main;protocol=ssh;user=git \
"
SRCREV = "6222ebb48ae44f473c9060efc258b87756734684"
S = "${WORKDIR}/git"

do_install:append() {
    rm -r ${D}/usr
}

