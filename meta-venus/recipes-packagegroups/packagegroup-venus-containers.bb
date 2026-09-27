SUMMARY = "Extra packages for container image"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = " \
    podman \
    podman-rootless \
    crun \
    iptables \
    slirp4netns \
    passt \
    netavark \
    aardvark-dns \
    dbus \
    dbus-dev \
    dbus-auth-proxy \
    venus-containers \
"
