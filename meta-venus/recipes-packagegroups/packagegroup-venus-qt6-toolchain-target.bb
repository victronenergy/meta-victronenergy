DESCRIPTION = "Target packages for Qt6 SDK"
LICENSE = "MIT"

inherit packagegroup

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"

RDEPENDS_${PN} += " \
    packagegroup-core-standalone-sdk-target \
    libsqlite3-dev \
    qtbase-dev \
    qtbase-plugins \
    qtbase-staticdev \
    qttranslations-qtbase \
    qtconnectivity-dev \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'qtconnectivity-qmlplugins', '', d)} \
    qttranslations-qtconnectivity \
    qtdeclarative-dev \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'qtdeclarative-qmlplugins', '', d)} \
    qtdeclarative-tools \
    qtdeclarative-staticdev \
    qtimageformats-dev \
    qtimageformats-plugins \
    qtserialport-dev \
    qtserialbus-dev \
    qtsvg-dev \
    qtsvg-plugins \
"
