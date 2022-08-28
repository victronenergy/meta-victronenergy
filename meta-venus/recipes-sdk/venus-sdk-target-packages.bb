SUMMARY = "TARGET packages for the SDK"
LICENSE = "MIT"

inherit packagegroup

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"
INSANE_SKIP:${PN} = "dev-deps"

# c and friends
RDEPENDS:${PN} += " \
	dbus-dev \
	libevent-dev \
	libusb1-dev \
	eudev-dev \
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
    qtbase-dev \
    qtconnectivity-dev \
    qtdeclarative-dev \
    qtimageformats-dev \
    qtmqtt-dev \
    qtserialport-dev \
    qtsvg-dev \
"
