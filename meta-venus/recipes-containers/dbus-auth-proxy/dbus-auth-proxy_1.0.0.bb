SUMMARY = "D-Bus authentication proxy for rootless container namespaces"
HOMEPAGE = "https://github.com/nmbath/dbus-auth-proxy"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=bb6e1dd6445715979d8a08ff718abf19"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "git://github.com/nmbath/dbus-auth-proxy.git;branch=master;protocol=https"
SRCREV = "0fc3d5f6f05615950054fb74bbcdcbcfcc5d95a7"
S = "${WORKDIR}/git"

inherit daemontools useradd

USERADD_PACKAGES = "${PN}"
USERADD_DEPENDS = "podman-rootless"
# venus-dbus has a static image GID below 2000. The 2000..2999 GID range is
# reserved for persistent post-image extension primary groups.
GROUPADD_PARAM:${PN} = "--system venus-dbus"
GROUPMEMS_PARAM:${PN} = "--group venus-dbus --add container"

RDEPENDS:${PN} = " \
    podman-rootless \
    python3-asyncio \
    python3-core \
"

DAEMONTOOLS_RUN = "python3 ${nonarch_libdir}/${BPN}/proxy.py"

do_install() {
    install -d ${D}${nonarch_libdir}/${BPN}
    install -m 0755 ${S}/src/proxy.py ${D}${nonarch_libdir}/${BPN}/
    install -m 0644 ${S}/src/identity.py ${D}${nonarch_libdir}/${BPN}/
    install -m 0644 ${S}/src/opts.py ${D}${nonarch_libdir}/${BPN}/
    install -m 0644 ${S}/version.py ${D}${nonarch_libdir}/${BPN}/
}
