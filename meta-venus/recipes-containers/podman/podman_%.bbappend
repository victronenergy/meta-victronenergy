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
EOF
    fi
}
