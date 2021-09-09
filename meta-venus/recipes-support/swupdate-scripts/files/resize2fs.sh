#!/bin/sh

. $(dirname $0)/functions.sh

root=$(get_rootdev $(get_rootfs))
if [ -z "$root" ]; then
    echo "Unable to determine rootfs. Exit."
    exit 1
fi

$(dirname "$0")/remount-rw.sh

fs=$(grep -E '^\S+\s+/\s' /proc/mounts | awk '{print $3}')
if [ "$fs" != "ext4" ]; then
    echo "Rootfs is not ext4, but $fs. Exit."
    exit 0
fi

echo "resizing $root"
resize2fs $root
