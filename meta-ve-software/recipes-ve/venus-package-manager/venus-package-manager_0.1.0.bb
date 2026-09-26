SUMMARY = "Extensible package manager for Venus OS"
DESCRIPTION = "D-Bus package lifecycle service and vpm management client"
HOMEPAGE = "https://github.com/nmbath/venus-package-manager"
LICENSE = "CLOSED"

SRC_URI = " \
    gitsm://github.com/nmbath/venus-package-manager.git;branch=main;protocol=ssh;user=git \
    file://venus-package-manager.conf \
"
SRCREV = "594d0323652c92bd017114dcdb333a023f1abf47"
S = "${WORKDIR}/git"

inherit allarch daemontools python-compile ve_package

RDEPENDS:${PN} = " \
    python3-core \
    python3-crypt \
    python3-dbus \
    python3-io \
    python3-json \
    python3-logging \
    python3-pygobject \
    nginx \
"

# The daemon owns the package registry and application deployment lifecycle,
# so it runs as root. vpm is an unprivileged D-Bus client.
DAEMONTOOLS_RUN = "${@softlimit(d, data=100000000, stack=1000000, all=100000000)} ${bindir}/venus-package-manager"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/venus-package-manager ${D}${bindir}/venus-package-manager
    install -m 0755 ${S}/vpm ${D}${bindir}/vpm
    install -m 0644 ${S}/*.py ${D}${bindir}/

    install -d ${D}${bindir}/ext/velib_python
    install -m 0644 ${S}/ext/velib_python/vedbus.py ${D}${bindir}/ext/velib_python/
    install -m 0644 ${S}/ext/velib_python/ve_utils.py ${D}${bindir}/ext/velib_python/

    install -d ${D}/usr/bin
    ln -sf ${bindir}/vpm ${D}/usr/bin/vpm

    install -d ${D}${sysconfdir}/nginx/locations-enabled
    install -m 0644 ${UNPACKDIR}/venus-package-manager.conf \
        ${D}${sysconfdir}/nginx/locations-enabled/venus-package-manager.conf
}

FILES:${PN} += " \
    /usr/bin/vpm \
    ${sysconfdir}/nginx/locations-enabled/venus-package-manager.conf \
"
