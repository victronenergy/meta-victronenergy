SUMMARY = "TARGET packages for the SDK"
LICENSE = "MIT"

PACKAGEGROUP_DISABLE_COMPLEMENTARY = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"
INSANE_SKIP:${PN} = "dev-deps"

inherit packagegroup

# c and friends
RDEPENDS:${PN} += " \
    avahi-dev \
    brotli-dev \
    dbus-dev \
    eudev-dev \
    gupnp-dev \
    libevdev-dev \
    libevent-dev \
    libusb1-dev \
    neatvnc-dev \
    iptables-dev \
    xorgproto-dev \
"

# compiler sanitizer plugins
RDEPENDS:${PN} += " \
    libasan-dev \
    libubsan-dev \
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
