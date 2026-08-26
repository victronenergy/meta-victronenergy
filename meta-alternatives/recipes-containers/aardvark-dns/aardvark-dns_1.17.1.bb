SUMMARY = "A container-focused DNS server"
HOMEPAGE = "https://github.com/containers/aardvark-dns"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRCREV = "880f6903d2259ce0049daed0985ae694f2c1b0ac"
S = "${WORKDIR}/git"

# It is possible to fetch the source using the crate fetcher instead:
#SRC_URI = "crate://crates.io/${BPN}/${PV}"
SRC_URI = "git://github.com/containers/aardvark-dns;protocol=https;nobranch=1 \
           file://run-ptest"
require ${BPN}-crates.inc

inherit cargo cargo-update-recipe-crates features_check ptest

# Cargo installs the binary to bin so move it to where podman expects it
do_install:append() {
	install -d ${D}${libexecdir}
	mv ${D}${bindir} ${D}${libexecdir}/podman
}

do_install_ptest() {
	cp -r ${S}/test ${D}${PTEST_PATH}
}

# rdepends on slirp4netns
REQUIRED_DISTRO_FEATURES ?= "seccomp"

RDEPENDS:${PN}-ptest += " \
    bash \
    bats \
    bind-utils \
    coreutils \
    dbus-daemon-proxy \
    ipcalc \
    iproute2 \
    jq \
    slirp4netns \
    util-linux-unshare \
"
