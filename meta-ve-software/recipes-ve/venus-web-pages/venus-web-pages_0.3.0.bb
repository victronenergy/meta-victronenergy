DESCRIPTION = "Venus OS embedded web-page registry and proxy service"
HOMEPAGE = "https://github.com/nmbath/venus-web-pages"
LICENSE = "CLOSED"

inherit allarch daemontools python-compile ve_package

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/nmbath/venus-web-pages.git;branch=main;protocol=ssh;user=git \
"
SRCREV = "4526639670b80d5c72be2c31dc7eefbdd36d8371"
S = "${WORKDIR}/git"

RDEPENDS:${PN} = " \
    nginx \
    python3-core \
    python3-dbus \
    python3-json \
    python3-logging \
    python3-pygobject \
"

# The daemon runs as root because it owns the persistent registry, allocates
# per-page listeners, writes and validates nginx configuration, and reloads
# nginx. vwp remains a lightweight D-Bus client with no privileged writes.
DAEMONTOOLS_RUN = "${@softlimit(d, data=100000000, stack=1000000, all=100000000)} ${bindir}/venus-web-pages"

do_install () {
    install -d ${D}${bindir}
    cp -r ${S}/* ${D}${bindir}
    rm -rf ${D}${bindir}/tests
    rm -f ${D}${bindir}/requirements-dev.txt

    # ve_package's application bindir is not on PATH. venus-exchange and
    # interactive users invoke the lightweight management client by name.
    install -d ${D}/usr/bin
    ln -sf ${bindir}/vwp ${D}/usr/bin/vwp
}

FILES:${PN} += "/usr/bin/vwp"
