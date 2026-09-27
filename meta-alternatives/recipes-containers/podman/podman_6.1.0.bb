HOMEPAGE = "https://podman.io/"
SUMMARY =  "A daemonless container engine"
DESCRIPTION = "Podman is a daemonless container engine for developing, \
    managing, and running OCI Containers on your Linux System. Containers can \
    either be run as root or in rootless mode. Simply put: \
    `alias docker=podman`. \
    "

inherit features_check
REQUIRED_DISTRO_FEATURES ?= "seccomp ipv6"

DEPENDS = " \
    gpgme \
    libseccomp \
    ${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)} \
    gettext-native \
"

# Pinned to the stable v6.1.0 release tag (containers/podman), not the
# floating "main" dev branch - meta-virtualization's own master tracks main
# for its 6.1.0-dev recipe, but a shipped Venus image needs a reproducible
# pin. nobranch=1 matches this layer's existing aardvark-dns override
# convention (fetch by exact commit, independent of any branch name).
SRCREV = "cade97a52ebdf9dbf9e81de8009015776837a074"
SRC_URI = " \
    git://github.com/containers/podman.git;protocol=https;nobranch=1 \
    ${@bb.utils.contains('PACKAGECONFIG', 'rootless', 'file://50-podman-rootless.conf', '', d)} \
"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/import/LICENSE;md5=3d9b931fa23ab1cacd0087f9e2ee12c0"

GO_IMPORT = "import"

S = "${WORKDIR}/git"

PV = "6.1.0"

CVE_STATUS[CVE-2019-10152] = "fixed-version: fixed since v1.4.0"
CVE_STATUS[CVE-2020-1726] = "fixed-version: fixed since v1.8.1"
CVE_STATUS[CVE-2022-2989] = "fixed-version: fixed since v4.3.0"
CVE_STATUS[CVE-2023-0778] = "fixed-version: fixed since v4.5.0"

PACKAGES =+ "${PN}-contrib"

PODMAN_PKG = "github.com/containers/podman"

# Podman's vendored containers/common library removed CNI support entirely
# (commit 8d1f636e40, March 2026). The network backend is now unconditionally
# netavark - the cni build tag is a no-op.
BUILDTAGS_EXTRA ?= ""
BUILDTAGS ?= "seccomp varlink \
${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
exclude_graphdriver_btrfs exclude_graphdriver_devicemapper ${BUILDTAGS_EXTRA}"

# overide LDFLAGS to allow podman to build without: "flag provided but not # defined: -Wl,-O1
export LDFLAGS = ""

# https://github.com/llvm/llvm-project/issues/53999
TOOLCHAIN = "gcc"

# podmans Makefile expects BUILDFLAGS to be set but go.bbclass defines them in GOBUILDFLAGS
export BUILDFLAGS = "${GOBUILDFLAGS} -buildvcs=false"

inherit go goarch
inherit container-host
inherit systemd pkgconfig

do_configure[noexec] = "1"

EXTRA_OEMAKE = " \
     PREFIX=${prefix} BINDIR=${bindir} LIBEXECDIR=${libexecdir} \
     ETCDIR=${sysconfdir} TMPFILESDIR=${nonarch_libdir}/tmpfiles.d \
     SYSTEMDDIR=${systemd_unitdir}/system USERSYSTEMDDIR=${systemd_user_unitdir} \
"

# remove 'docker' from the features if you don't want podman to
# build and install the docker wrapper. If docker is enabled in the
# variable, the podman package will rconfict with docker.
PODMAN_FEATURES ?= "docker"

PACKAGECONFIG ?= ""
PACKAGECONFIG[rootless] = ",,,fuse-overlayfs passt,,"

do_compile() {
	cd ${S}/src
	rm -rf .gopath
	mkdir -p .gopath/src/"$(dirname "${PODMAN_PKG}")"
	ln -sf ../../../../import/ .gopath/src/"${PODMAN_PKG}"

	ln -sf "../../../import/vendor/github.com/varlink/" ".gopath/src/github.com/varlink"

	export GOARCH="${BUILD_GOARCH}"
	export GOPATH="${S}/src/.gopath"
	export GOROOT="${STAGING_DIR_NATIVE}/${nonarch_libdir}/${HOST_SYS}/go"

	cd ${S}/src/.gopath/src/"${PODMAN_PKG}"

	# Pass the needed cflags/ldflags so that cgo
	# can find the needed headers files and libraries
	export GOARCH=${TARGET_GOARCH}
	export CGO_ENABLED="1"
	export CGO_CFLAGS="${CFLAGS}"
	export CGO_LDFLAGS="${LDFLAGS}"

	# podman now builds go-md2man and requires the host/build details
	export NATIVE_GOOS=${BUILD_GOOS}
	export NATIVE_GOARCH=${BUILD_GOARCH}

	oe_runmake NATIVE_GOOS=${BUILD_GOOS} NATIVE_GOARCH=${BUILD_GOARCH} BUILDTAGS="${BUILDTAGS}"
}

do_install() {
	cd ${S}/src/.gopath/src/"${PODMAN_PKG}"

	export GOARCH="${BUILD_GOARCH}"
	export GOPATH="${S}/src/.gopath"
	export GOROOT="${STAGING_DIR_NATIVE}/${nonarch_libdir}/${HOST_SYS}/go"

	oe_runmake install DESTDIR="${D}"
	if ${@bb.utils.contains('PODMAN_FEATURES', 'docker', 'true', 'false', d)}; then
		oe_runmake install.docker DESTDIR="${D}"
	fi

	# Silence docker emulation warnings.
	mkdir -p ${D}/etc/containers
	touch ${D}/etc/containers/nodocker

	if ${@bb.utils.contains('PACKAGECONFIG', 'rootless', 'true', 'false', d)}; then
		install -d "${D}${sysconfdir}/sysctl.d"
		install -m 0644 "${UNPACKDIR}/50-podman-rootless.conf" "${D}${sysconfdir}/sysctl.d"
		install -d "${D}${sysconfdir}/containers"
		cat <<-EOF >> "${D}${sysconfdir}/containers/containers.conf"
		[NETWORK]
		default_rootless_network_cmd="pasta"
		# netavark otherwise invokes pasta with --no-map-gw, leaving a
		# rootless container's own default-route gateway address a dead
		# end rather than a path to the host (confirmed live on
		# venus.local, e.g. against the host's own sshd). --map-gw splices
		# traffic aimed at that address onto the host's real loopback
		# interface instead, same idea as Docker's host.docker.internal -
		# still subject to whatever the host firewall does with
		# loopback-sourced connections, not a new hole beyond that.
		#
		# -T/-U auto (confirmed live on venus.local, 2026-09-01): the one
		# shared "rootless-netns" pasta instance per identity - the process
		# bridging every bridge network for that identity to the real host
		# - is otherwise invoked by Podman with -t/-u/-T/-U all forced to
		# "none". -T/-U specifically control forwarding *to* the host/init
		# namespace, i.e. exactly the direction a venus-containers managed
		# child needs to reach host.containers.internal; without this
		# override nothing a child sends toward it ever reaches the real
		# host at all (confirmed by a bare connection timeout, not just an
		# application-level failure). "auto" (not a wide explicit port
		# range - pasta opens one socket per listed port, and this
		# platform's default ulimit -n of 1024 is nowhere near enough for
		# a real range) would otherwise conflict with aardvark-dns's own
		# use of that same namespace's port 53 for a bridge network's DNS,
		# but venus-containers' own child-scope networks pass
		# --disable-dns (child-to-child resolution is already static
		# --add-host, never DNS), so nothing is left to conflict with here.
		pasta_options=["--map-gw", "-T", "auto", "-U", "auto"]
		EOF
	fi
}

FILES:${PN} += " \
    ${systemd_unitdir}/system/* \
    ${nonarch_libdir}/systemd/* \
    ${systemd_user_unitdir}/* \
    ${nonarch_libdir}/tmpfiles.d/* \
    ${datadir}/user-tmpfiles.d/* \
"

SYSTEMD_SERVICE:${PN} = "podman.service podman.socket"

# The other option for this is "busybox", since meta-virt ensures
# that busybox is configured with nsenter
VIRTUAL-RUNTIME_base-utils-nsenter ?= "util-linux-nsenter"

COMPATIBLE_HOST = "^(?!mips).*"

# netavark is the only supported network backend since podman 6.0
VIRTUAL-RUNTIME_container_networking = "netavark"
VIRTUAL-RUNTIME_container_dns = "aardvark-dns"

# use crun for the OCI runtime
VIRTUAL-RUNTIME_container_runtime = "crun"

RDEPENDS:${PN} += "\
	catatonit conmon ${VIRTUAL-RUNTIME_container_runtime} gpgme iptables libdevmapper \
	${VIRTUAL-RUNTIME_container_networking} ${VIRTUAL-RUNTIME_container_dns} ${VIRTUAL-RUNTIME_base-utils-nsenter} \
"
RRECOMMENDS:${PN} += "kernel-module-xt-masquerade \
                      kernel-module-xt-comment \
                      kernel-module-xt-mark \
                      kernel-module-xt-addrtype \
                      kernel-module-xt-conntrack \
                      kernel-module-xt-tcpudp \
                      "
RCONFLICTS:${PN} = "${@bb.utils.contains('PACKAGECONFIG', 'docker', 'docker', '', d)}"
