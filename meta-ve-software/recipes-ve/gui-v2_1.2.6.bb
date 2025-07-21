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
RDEPENDS_${DAEMON_PN} = "${PN}"

DAEMONTOOLS_SCRIPT = ". /etc/profile.d/qt6.sh && exec softlimit -d 768000000 -s 1000000 -a 768000000 ${bindir}/venus-gui-v2"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/gui-v2.git;branch=main;protocol=ssh;user=git \
    file://0001-CMakeLists.txt-make-lupdate-depend-on-src-themeobjec.patch \
"
SRCREV = "2c261f0c3cae01838ba42aee3d8aa64794e96241"
S = "${WORKDIR}/git"

do_install:append() {
    rm -r ${D}/usr
}

