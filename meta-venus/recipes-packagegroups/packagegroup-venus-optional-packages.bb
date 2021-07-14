# build but not installed in the image
include qt5-packages.inc

inherit packagegroup
LICENSE = "MIT"

DEPENDS += "\
    devmem2 \
    gdb \
    gpsd \
    git \
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
    s6 \
    tcpdump \
    tinymembench \
    tmux \
    valgrind \
    venus-socketcan-test \
    vim \
    x11vnc \
"

# disable for now for the rpi's, linux must be updated
DEPENDS_remove_raspberrypi2 = "perf"
DEPENDS_remove_raspberrypi4 = "perf"

RDEPENDS_${PN} += " \
    ${QT5_PACKAGES} \
"
