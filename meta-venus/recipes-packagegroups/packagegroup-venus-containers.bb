SUMMARY = "Extra packages for container image"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = " \
    podman \
    crun \
    iptables \
    slirp4netns \
"

# D-Bus for Venus integration
RDEPENDS:${PN} += " \
    dbus \
    dbus-dev \
"
