# Venus does not expose Docker compatibility
PACKAGECONFIG:remove = "docker"

# Device-mapper graph driver is already excluded and
# Venus uses dedicated filesystem/overlay storage.
RDEPENDS:${PN}:remove = "libdevmapper"

# Support rootless
PACKAGECONFIG:append = " rootless"

# Venus has no systemd (crun is built with --disable-systemd), so there is
# no unit to delegate a per-user cgroup to a rootless podman user. Tell
# podman/crun to manage the v2 cgroup directly instead of assuming
# delegation. Appended the same way upstream's own do_install appends its
# [NETWORK] section to this file.
do_install:append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'rootless', 'true', 'false', d)}; then
        cat <<EOF >> "${D}${sysconfdir}/containers/containers.conf"
[engine]
cgroup_manager = "cgroupfs"
events_logger = "file"

[containers]
# venus-containers gives each managed container a canonical k8s-file under
# /var/log/containers and rotates it at the same 25000-byte threshold used by
# daemontools services, retaining four historic files. Keep this larger Podman
# limit as an emergency backstop if the manager is unavailable: k8s-file has
# no retained-history rotation of its own and otherwise grows without bound on
# the /data partition.
log_size_max = 100000
EOF
    fi
}
