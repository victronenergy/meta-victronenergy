DESCRIPTION = "Definition-driven OCI container management for Venus OS"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e11dcd0c434a2a2ca5e7587d67ce0edb"

inherit ve_package
inherit daemontools
inherit python-compile

# venus-containers is a private repo (github.com/nmbath/venus-containers) -
# fetched over SSH using a dedicated read-only deploy key, not the build
# machine's own key (which is already in use elsewhere and has no access
# to this repo). "github.com-venus-containers" is a Host alias in this
# build machine's ~/.ssh/config pointing at that key
# (~/.ssh/venus_containers_deploy) - it is NOT a real hostname, and this
# recipe will not fetch on a machine without that config entry and key.
#
# PV comes from the filename (dbus-containers_0.1.bb), matching
# dbus-systemcalc-py/dbus-generator/dbus-modem's own recipes exactly - none
# of them set PV or use a "_git.bb"/"+git" floating-version naming either.
# version.py (softwareversion) in the repo is the single source of truth
# this filename has to be kept in sync with by hand until there are real
# git tags to drive UPSTREAM_CHECK_GITTAGREGEX from instead.
UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com-venus-containers/nmbath/venus-containers.git;branch=main;protocol=ssh;user=git \
"
SRCREV = "0d9dbf5848a77fe3e47632ef3d4e903827fd653d"
S = "${WORKDIR}/git"
# The on-disk product directory follows the repository/product name rather
# than PN, which remains dbus-containers for the D-Bus service and package.
bindir = "${vedir}/venus-containers"
CONTAINER_EXAMPLES_DIR = "${bindir}/examples"

RDEPENDS:${PN} = " \
    localsettings \
    python3-core \
    python3-dbus \
    python3-pygobject \
    podman \
    podman-rootless \
    crun \
    dbus-auth-proxy \
"

# Verified on-device (Podman 5.0.3): resource limits/stats only work if the
# process is joined to the delegated cgroup - plain setpriv/su is not
# enough. run-as-container (podman-rootless_1.0.bb) does both the cgroup
# join and the privilege drop; it's on PATH as a normal /usr/bin script
# (podman-rootless doesn't inherit ve_package, so its bindir is the OE
# default), unlike this package's own ${bindir}.
#
# No softlimit wrapper here (unlike dbus-systemcalc-py/dbus_generator):
# this daemon shells out to podman as a subprocess, and daemontools'
# softlimit rlimits are inherited by children - an address-space/data
# limit sized for a small Python daemon could starve a podman invocation
# in ways that are hard to predict without testing under real load. Worth
# revisiting once there's a real device to load-test against.
DAEMONTOOLS_RUN = "run-as-container ${bindir}/dbus-containers"
DAEMONTOOLS_DOWN = "1"

do_install () {
    install -d ${D}${bindir}
    cp -r ${S}/* ${D}${bindir}
    rm -rf ${D}${bindir}/tests ${D}${bindir}/docs

    # Install the UUID-free example definitions separately from the developer
    # documentation so they are available as registration inputs on target.
    install -d ${D}${CONTAINER_EXAMPLES_DIR}
    install -m 0644 ${S}/docs/examples/*.json ${D}${CONTAINER_EXAMPLES_DIR}/
    install -m 0644 ${S}/docs/examples/README.md ${D}${CONTAINER_EXAMPLES_DIR}/

    # venus-containers (the CLI) needs to be on PATH for users/scripts to
    # call it by name; dbus-containers (the daemon) doesn't, since
    # daemontools invokes it by full path via DAEMONTOOLS_RUN above.
    # Confirmed on-device that ve_package's application bindir is never on PATH
    # (PATH=/usr/bin:/bin:/usr/sbin:/sbin only), so this symlink is
    # required, not cosmetic. Built at package time here rather than via
    # pkg_postinst_ontarget, per Victron convention.
    install -d ${D}/usr/bin
    ln -sf ${bindir}/venus-containers ${D}/usr/bin/venus-containers
}

FILES:${PN} += " \
    /usr/bin/venus-containers \
    ${CONTAINER_EXAMPLES_DIR} \
"
