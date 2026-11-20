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
RDEPENDS:${PN}-plugin-compiler += " \
    python3-core \
    python3-json \
    python3-netclient \
    python3-shell \
    python3-xml \
    qtbase-tools \
    qttools-tools \
"

PACKAGES =+ "start-gui-v2 ${PN}-plugin-compiler"
DAEMON_PN = "start-gui-v2"
RDEPENDS:${DAEMON_PN} = "${PN}"

DAEMONTOOLS_SCRIPT = ". /etc/profile.d/qt6.sh && exec ${@softlimit(d, data=768000000, stack=1000000, all=768000000)} ${bindir}/venus-gui-v2"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/gui-v2.git;branch=main;protocol=ssh;user=git \
    file://0001-GUI-plugins-install-gui-v2-plugin-compiler.py-as-a-p.patch \
"
SRCREV = "8e7132e6fd94f22aaa218672594cb3494212ea2a"
S = "${WORKDIR}/git"

do_install:append() {
    rm -r ${D}/usr
}

FILES:${PN}-plugin-compiler = " \
    ${bindir}/gui-v2-plugin-compiler.py \
    ${bindir}/branding-properties.json \
    ${bindir}/guiplugins-api.json \
"
