DESCRIPTION = "Definition-driven OCI container management for Venus OS"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e11dcd0c434a2a2ca5e7587d67ce0edb"

inherit ve_package
inherit daemontools
inherit python-compile

# venus-containers is a private repository. Fetch it from GitHub over SSH;
# access is provided through the normal GitHub SSH identity.
#
# PV comes from the filename (venus-containers_0.8.0.bb), matching
# dbus-systemcalc-py/dbus-generator/dbus-modem's own recipes exactly - none
# of them set PV or use a "_git.bb"/"+git" floating-version naming either.
# version.py (softwareversion) in the repo is the single source of truth
# this filename has to be kept in sync with by hand until there are real
# git tags to drive UPSTREAM_CHECK_GITTAGREGEX from instead.
UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/nmbath/venus-containers.git;branch=main;protocol=ssh;user=git \
"

# gitsm derives submodule fetch URLs directly from .gitmodules. Redirect the
# private venus-identities HTTPS URL to normal GitHub SSH authentication; the
# public velib_python submodule continues to use HTTPS.
PREMIRRORS:prepend = "gitsm://github.com/nmbath/venus-identities.git gitsm://github.com/nmbath/venus-identities.git;protocol=ssh;user=git \n"

SRCREV = "ab74e511026f83a82e39245eea94c95f821a4d9c"
S = "${WORKDIR}/git"
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
    venus-identities \
    venus-storage \
"

# Runs as root, not wrapped in podman-rootless's run-as-container: this
# daemon does its own per-operation identity switch to the rootless
# "container" user for each podman invocation (backend/execution.py),
# including joining the cgroup v2 leaf delegated to that identity so
# --memory/--cpus/--pids-limit actually take effect (verified on-device,
# Podman 5.0.3 - plain setpriv/su is not enough on its own). Wrapping the
# whole daemon in run-as-container instead would drop its own privilege
# before it can do that per-operation switch at all.
#
# No softlimit wrapper here (unlike dbus-systemcalc-py/dbus_generator):
# this daemon shells out to podman as a subprocess, and daemontools'
# softlimit rlimits are inherited by children - an address-space/data
# limit sized for a small Python daemon could starve a podman invocation
# in ways that are hard to predict without testing under real load. Worth
# revisiting once there's a real device to load-test against.
DAEMONTOOLS_RUN = "${bindir}/dbus-containers"
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

    # vcm (the CLI, renamed from venus-containers on 2026-08-31 for typing
    # convenience) needs to be on PATH for users/scripts to call it by
    # name; dbus-containers (the daemon) doesn't, since daemontools invokes
    # it by full path via DAEMONTOOLS_RUN above. Confirmed on-device that
    # ve_package's application bindir is never on PATH
    # (PATH=/usr/bin:/bin:/usr/sbin:/sbin only), so this symlink is
    # required, not cosmetic. Built at package time here rather than via
    # pkg_postinst_ontarget, per Victron convention.
    install -d ${D}/usr/bin
    ln -sf ${bindir}/vcm ${D}/usr/bin/vcm
}

FILES:${PN} += " \
    /usr/bin/vcm \
    ${CONTAINER_EXAMPLES_DIR} \
"
