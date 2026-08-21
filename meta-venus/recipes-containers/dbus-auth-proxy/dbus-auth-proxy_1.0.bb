SUMMARY = "Proxy that fixes D-Bus AUTH EXTERNAL UID mismatches for rootless containers"
DESCRIPTION = "Runs as the rootless container user and rewrites the AUTH \
EXTERNAL UID in D-Bus handshakes coming from containers, so the system \
bus daemon sees a UID that matches the connection's real (host-mapped) \
socket credentials instead of the containers internal UID."
HOMEPAGE = "https://github.com/nmbath/dbus-auth-proxy"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE.md;md5=bb6e1dd6445715979d8a08ff718abf19"

# Fork of https://github.com/yichenshen/dbus-auth-proxy: the upstream tip
# crashes on every start (missing Awaitable import) and only ever trusts
# connections whose host-mapped UID exactly matches the proxy's own. The
# fork's three commits on top fix the crash, add an opt-in trusted subuid
# range, and chmod the socket so a differently-UID-mapped process can
# actually connect in the first place.
SRC_URI = "git://github.com/nmbath/dbus-auth-proxy;branch=master;protocol=https"
SRCREV = "362586970ab035b32c9cfac5dbb6091ae657156f"
S = "${WORKDIR}/git"

inherit daemontools

RDEPENDS:${PN} = " \
    python3-core \
    python3-asyncio \
    util-linux-setpriv \
"

# The rootless "container" user (see podman-rootless_1.0.bb). Socket lives
# under /run/user/1002 (tmpfs, recreated every boot by podman-rootless's
# volatiles fragment).
DBUS_AUTH_PROXY_SOCKET_DIR = "/run/user/1002/dbus-auth-proxy"

# Matches the container user's own /etc/subuid grant (see the
# shadow_%.bbappend commit): a container's internal UID 0 maps to the
# container user's own UID (1002, handled by the proxy's default identity
# check) while any other internal UID - e.g. an image's normal non-root
# user, or one an entrypoint drops to after startup - maps somewhere in
# this range instead.
DBUS_AUTH_PROXY_TRUSTED_RANGE_START = "100000"
DBUS_AUTH_PROXY_TRUSTED_RANGE_SIZE = "65536"

do_install() {
    install -d ${D}${libdir}/dbus-auth-proxy
    install -m 0644 ${S}/src/proxy.py ${D}${libdir}/dbus-auth-proxy/proxy.py
    install -m 0644 ${S}/src/opts.py ${D}${libdir}/dbus-auth-proxy/opts.py
}

FILES:${PN} = "${libdir}/dbus-auth-proxy"

DAEMONTOOLS_SCRIPT = "mkdir -p ${DBUS_AUTH_PROXY_SOCKET_DIR} && chown container:container ${DBUS_AUTH_PROXY_SOCKET_DIR} && exec setpriv --init-groups --reuid container --regid container env CLIENT_SOCKET=${DBUS_AUTH_PROXY_SOCKET_DIR}/system_bus_socket TRUSTED_UID_RANGE_START=${DBUS_AUTH_PROXY_TRUSTED_RANGE_START} TRUSTED_UID_RANGE_SIZE=${DBUS_AUTH_PROXY_TRUSTED_RANGE_SIZE} python3 ${libdir}/dbus-auth-proxy/proxy.py"
