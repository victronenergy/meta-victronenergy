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

is_mounted() {
    awk -v path="$1" '$2 == path { found = 1 } END { exit !found }' /proc/mounts
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
        # Without CAP_SYS_ADMIN, mountall cannot mount the tmpfs entries from
        # /etc/fstab. Clear the container overlay instead to give every boot
        # the same volatile semantics.
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

# Without CAP_SYS_ADMIN overlays.sh cannot bind-mount its service copy on
# /service, and clearing /run above may have removed that copy.
if ! is_mounted /service; then
    mkdir -p /run/overlays/service
    cp -a /opt/victronenergy/service/* /run/overlays/service
    [ -L /service ] || { rm -rf /service; ln -s /run/overlays/service /service; }
fi
