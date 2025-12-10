inherit qt6-qmake pkgconfig

SRC_URI = "git://github.com/jhofstee/qml-module-jvterm.git;protocol=https;branch=master"
SRCREV = "ec825b50beb582b256cc292d27d8da1946ea558b"
S = "${WORKDIR}/git"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.MIT;md5=f71155f18b4006ffefd262e0fd046bad"

DEPENDS = "qtbase qtdeclarative qttools-native libvterm"

DESTDIR = "${D}/${QT6_INSTALL_QMLDIR}/jhofstee/nl/VTerm"

do_install:prepend() {
    # I give up, for some reason the library fails to install in OE.
    # Might be version related, or due to the use of INSTALL_ROOT in OE
    # or ... Anyway not going to waste more time on this, only 3 files need to
    # be installed (2 actually, the qmltypes is optional)
    mkdir -p ${DESTDIR}
    install -m 755 ${B}/qml-module-jvterm/libjvtermplugin.so ${DESTDIR}
    install -m 644 ${B}/qml-module-jvterm/qmldir ${B}/qml-module-jvterm/plugins.qmltypes ${DESTDIR}
}

FILES:${PN} += "${QT6_INSTALL_QMLDIR}"

