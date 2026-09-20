#!/bin/sh

### BEGIN INIT INFO
# Provides:          container-volatile
# Required-Start:    mountall
# Required-Stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: Restore tmpfs semantics when a container cannot mount tmpfs
### END INIT INFO

is_tmpfs() {
    awk -v path="$1" '$2 == path && $3 == "tmpfs" { found = 1 } END { exit !found }' /proc/mounts
}

clear_unmounted_volatile_dir() {
    path=$1

    if ! is_tmpfs "$path"; then
        # HA Supervisor does not grant CAP_SYS_ADMIN, so mountall cannot
        # create the tmpfs entries from /etc/fstab.  Clear the container
        # overlay instead to give every boot the same volatile semantics.
        # -xdev preserves Supervisor bind mounts such as /run/cid.
        find "$path" -xdev -mindepth 1 -delete 2>/dev/null || true
    fi
}

clear_unmounted_volatile_dir /run
clear_unmounted_volatile_dir /var/volatile
