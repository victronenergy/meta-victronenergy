SUMMARY = "A hw-accelerated, touch only UI for Venus devices."

include gui-v2.inc

inherit daemontools qt6-cmake start-gui ve_package

DEPENDS += "qtdeclarative-native qttools-native"
DEPENDS += "qt5compat qtbase qtdeclarative qtmqtt qtshadertools qtsvg qtvirtualkeyboard"
RDEPENDS:${PN} = " \
    qt5compat-qmlplugins \
    qtdeclarative-qmlplugins \
    qtvirtualkeyboard-qmlplugins \
    venus-ui-themes \
"

PACKAGES += "start-gui-v2"
DAEMON_PN = "start-gui-v2"
RDEPENDS_${DAEMON_PN} = "${PN}"

DAEMONTOOLS_SCRIPT = ". /etc/profile.d/qt6.sh && exec softlimit -d 768000000 -s 1000000 -a 768000000 ${bindir}/venus-gui-v2"

SRC_URI = " \
	gitsm://github.com/victronenergy/gui-v2.git;branch=v3.50;protocol=ssh;user=git;tag=v${PV} \
"
S = "${WORKDIR}/git"
EXTRA_OECMAKE = "-DNO_CACHEGEN=true -DLOAD_QML_FROM_FILESYSTEM=true"

do_install:append() {
	rm -r ${D}/usr
}

