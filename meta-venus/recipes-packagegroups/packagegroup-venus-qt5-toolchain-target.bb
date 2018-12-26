# Copyright (C) 2014 O.S. Systems Software LTDA.

DESCRIPTION = "Target packages for Qt5 SDK"
LICENSE = "MIT"

inherit packagegroup

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"

RDEPENDS_${PN} += " \
    packagegroup-core-standalone-sdk-target \
    libsqlite3-dev \
    qtbase-dev \
    qtbase-mkspecs \
    qtbase-plugins \
    qtbase-staticdev \
    qttranslations-qtbase \
    qttranslations-qthelp \
    qtconnectivity-dev \
    qtconnectivity-mkspecs \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'qtconnectivity-qmlplugins', '', d)} \
    qttranslations-qtconnectivity \
    qtdeclarative-dev \
    qtdeclarative-mkspecs \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'qtdeclarative-qmlplugins', '', d)} \
    qtdeclarative-tools \
    qtdeclarative-staticdev \
    qttranslations-qtdeclarative \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'qtgraphicaleffects-qmlplugins', '', d)} \
    qtimageformats-dev \
    qtimageformats-plugins \
    qtserialport-dev \
    qtserialport-mkspecs \
    qtserialbus-dev \
    qtserialbus-mkspecs \
    qtsvg-dev \
    qtsvg-mkspecs \
    qtsvg-plugins \
    qtsystems-dev \
    qtsystems-mkspecs \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'qtsystems-qmlplugins', '', d)} \
    qtquick1-dev \
    qtquick1-mkspecs \
    qtquick1-plugins \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'qtquick1-qmlplugins', '', d)} \
    qttranslations-qtwebsockets \
    qttranslations-qtxmlpatterns \
    qtxmlpatterns-dev \
    qtxmlpatterns-mkspecs \
    qtquickcontrols2 \
    qtquickcontrols2-dev \
    qtquickcontrols2-mkspecs \
"

RRECOMMENDS_${PN} += " \
    qtquickcontrols-qmlplugins \
"
