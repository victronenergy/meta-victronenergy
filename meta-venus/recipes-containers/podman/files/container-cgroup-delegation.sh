# Delegates cgroup v2 controllers to the rootless "container" user.
#
# podman/crun run with cgroup_manager=cgroupfs (see podman_%.bbappend) since
# there is no systemd here to auto-delegate a per-user cgroup the way
# systemd-logind normally would. Without this, the container user's
# processes just live in the root cgroup, which it has no write access to,
# so --memory/--cpus/--pids-limit etc on podman run are silently
# unenforced.
#
# container.slice is delegation-only (its subtree_control is populated, so
# cgroup v2's "no internal process constraint" forbids it from ever holding
# member processes directly). container.slice/session is the leaf the
# container user's processes actually join; crun creates and manages its
# own further-nested cgroup under there per container, enabling whatever
# subtree_control it needs dynamically.

for controller in cpu memory pids; do
	grep -qw "$controller" /sys/fs/cgroup/cgroup.subtree_control || echo "+$controller" > /sys/fs/cgroup/cgroup.subtree_control
done

mkdir -p /sys/fs/cgroup/container.slice/session
chown container:container /sys/fs/cgroup/container.slice /sys/fs/cgroup/container.slice/cgroup.procs /sys/fs/cgroup/container.slice/cgroup.subtree_control
chown container:container /sys/fs/cgroup/container.slice/session /sys/fs/cgroup/container.slice/session/cgroup.procs /sys/fs/cgroup/container.slice/session/cgroup.subtree_control

for controller in cpu memory pids; do
	echo "+$controller" > /sys/fs/cgroup/container.slice/cgroup.subtree_control
done
