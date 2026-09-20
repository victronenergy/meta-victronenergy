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

contains_mountpoint() {
    path=$1

    awk -v path="$path" '
        {
            mountpoint = $5
            gsub(/\\040/, " ", mountpoint)
            gsub(/\\011/, "\t", mountpoint)

            if (mountpoint == path || index(mountpoint, path "/") == 1)
                found = 1
        }
        END { exit !found }
    ' /proc/self/mountinfo
}

clear_unmounted_volatile_dir() {
    path=$1

    if ! is_tmpfs "$path"; then
        # HA Supervisor does not grant CAP_SYS_ADMIN, so mountall cannot
        # create the tmpfs entries from /etc/fstab.  Clear the container
        # overlay instead to give every boot the same volatile semantics.
        #
        # Preserve each top-level entry that is, or contains, a mountpoint.
        # -xdev is not sufficient because a bind mount can use the same
        # filesystem as its parent.
        for entry in "$path"/* "$path"/.[!.]* "$path"/..?*; do
            [ -e "$entry" ] || [ -L "$entry" ] || continue

            if contains_mountpoint "$entry"; then
                continue
            fi

            rm -rf "$entry"
        done
    fi
}

clear_unmounted_volatile_dir /run
clear_unmounted_volatile_dir /var/volatile
