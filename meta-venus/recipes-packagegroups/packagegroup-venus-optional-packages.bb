# build but not installed in the image
include qt6-packages.inc

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

DEPENDS += "\
    bc \
    devmem2 \
    gdb \
    dfu-util \
    gpsd \
    git \
    kmscube \
    lsof \
    nodejs \
    ntp \
    openvpn \
    packagegroup-core-buildessential \
    packagegroup-replace-busybox \
    perf \
    python3-pip \
    python3-pylint \
    python3-spidev \
    qtmqtt \
    s6 \
    start-fingerpaint \
    start-sway \
    tcpdump \
    tinymembench \
    tmux \
    valgrind \
    venus-socketcan-test \
    vim \
    wayvnc \
    x11vnc \
"

RDEPENDS:${PN} += " \
    ${QT6_PACKAGES} \
"
