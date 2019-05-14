SUMMARY = "Base packages for venus."
DESCRIPTION = " \
    This packagegroup should contain packages / tools etc for the base \
    venus system to get a bootable system with common tools, common for \
    all MACHINE's we support, but not doing anything useful and build \
    base packages available for download. High level applications typically \
    should not be here, since they can run on Angstrom / Debian etc as well. \
    Or to put it the other way around, packages which are basic distro \
    choises end up here. \
    \
    The image determines what to ship in the end, not suprisingly a venus \
    image would ship venus-base + ve-*, but shipping a angstrom-base + \
    ve-*-apps should work as well (at least in theory) \
    \
    note: machine specific venus base recipes are in packagegroup-venus-machine \
    (yes the base is missing, but it is already a long enough name. It is just \
    to make this packagegroup not machine specific, so bitbake doesn't \
    deinstall and reinstall everything when switching MACHINE's, but can just leave \
    or use the sstate package) \
    \
    note: For machines without any display or monitor simple-upnp / \
    javascript-vnc-client suddenly become base packages, since it is the / \
    only way to connect with them easily. I guess those should be moved to \
    packagegroup-venus-machine and depend on a MACHINE feature. \
"

inherit packagegroup
LICENSE = "MIT"

RDEPENDS_${PN} += "\
    avahi-autoipd \
    bash \
    bluez5 \
    bluez5-noinst-tools \
    bzip2 \
    ca-certificates \
    canutils \
    can-utils \
    connman \
    connman-tools \
    cronie \
    curl \
    custom-rc.d \
    dbus \
    dbus-tools \
    default-locale \
    dnsmasq \
    dpkg-start-stop \
    e2fsprogs \
    eglibc-utils \
    ftdi-control \
    gdbserver \
    haveged \
    iproute2 \
    iw \
    javascript-vnc-client \
    ldd \
    less \
    linux-firmware-rt2800 \
    linux-firmware-rt73 \
    linux-firmware-rtl8192cu \
    llmnrd \
    localedef \
    logo \
    nano \
    ncurses-tools \
    openssh \
    openssh-sftp-server \
    opkg \
    permanent-storage \
    pointercal \
    ppp \
    python-pymodbus \
    python-pyserial \
    rsync \
    rtl8192eu \
    screen \
    socat \
    strace \
    sysctl-conf \
    tzdata \
    tzdata-africa \
    tzdata-americas \
    tzdata-antarctica \
    tzdata-arctic \
    tzdata-asia \
    tzdata-atlantic \
    tzdata-australia \
    tzdata-europe \
    tzdata-pacific \
    usbutils \
    udev-extraconf \
    venus-feed-configs \
    venus-html5-app \
    venus-version \
    watchdog \
    websockify-c \
    wget \
    wireless-regdb-static \
    zip \
"
