SUMMARY = "Extra packages for container image"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = " \
    podman \
    podman-rootless \
    crun \
    iptables \
    slirp4netns \
"

# Rootless container networking (Podman 6's only supported backend)
RDEPENDS:${PN} += " \
    netavark \
    aardvark-dns \
"

# D-Bus for Venus integration
RDEPENDS:${PN} += " \
    dbus \
    dbus-dev \
    dbus-auth-proxy \
"
