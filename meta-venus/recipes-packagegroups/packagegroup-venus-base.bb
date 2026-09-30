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
    note: hardware support, such as networking, wireless firmware, \
    a local display and the watchdog, and other machine specific \
    packages are in packagegroup-venus-machine. \
"

# See commit 5bf3e447, packagegroups with dynamic package, like dbus, should be
# tune specific.
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} += "\
    bash \
    bzip2 \
    ca-certificates \
    can-utils \
    can-utils-access \
    can-utils-cantest \
    crash-logger \
    cronie \
    curl \
    custom-rc.d \
    dbus \
    dbus-common \
    dbus-tools \
    default-locale \
    dpkg-start-stop \
    e2fsprogs \
    eglibc-utils \
    ftdi-control \
    gdbserver \
    io-extender \
    iproute2 \
    ldd \
    less \
    llmnrd \
    localedef \
    logo \
    nano \
    ncurses-tools \
    openssh \
    openssh-sftp-server \
    opkg \
    permanent-storage \
    python-symlink \
    rsync \
    screen \
    socat \
    sysctl-conf \
    tzdata-africa \
    tzdata-americas \
    tzdata-antarctica \
    tzdata-arctic \
    tzdata-asia \
    tzdata-atlantic \
    tzdata-australia \
    tzdata-core \
    tzdata-europe \
    tzdata-pacific \
    usbutils \
    udev-extraconf \
    util-linux-hwclock \
    util-linux-mount \
    venus-dbus-tools \
    venus-feed-configs \
    venus-html5-app \
    venus-mdns-aliases \
    wget \
    zip \
"

RDEPENDS:${PN} += "python3-s2"

# netmon provides a workaround for an problem solved years ago
# and it is a open issue if the package shouldn't be dropped.
# Since it requires quite some resources with the scarthgap
# python version, drop it for the devices with little memory.
NETMON = " netmon"
NETMON:ccgx = ""
NETMON:canvu500 = ""
RDEPENDS:${PN}:append = "${NETMON}"

DBUS_SHELLY = " dbus-shelly"
DBUS_SHELLY:ccgx = ""
RDEPENDS:${PN}:append = "${DBUS_SHELLY}"
