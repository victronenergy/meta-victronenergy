SUMMARY = "TARGET packages for the SDK"
LICENSE = "MIT"

inherit packagegroup

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"
INSANE_SKIP:${PN} = "dev-deps"
PACKAGE_ARCH = ""

# c and friends
RDEPENDS:${PN} += " \
    avahi-dev \
    dbus-dev \
    eudev-dev \
    gupnp-dev \
    libevent-dev \
    libusb1-dev \
    neatvnc-dev \
"

# qt4
RDEPENDS:${PN} += " \
    qt4-embedded-mkspecs \
    libqt-embeddedcore4-dev \
    libqt-embeddeddbus4-dev \
    libqt-embeddeddeclarative4-dev \
    libqt-embeddedgui4-dev \
    libqt-embeddedsvg4-dev \
"

# qt6
RDEPENDS:${PN} += " \
    qt5compat-dev \
    qtbase-dev \
    qtconnectivity-dev \
    qtdeclarative-dev \
    qtimageformats-dev \
    qtmqtt-dev \
    qtserialport-dev \
    qtsvg-dev \
"
