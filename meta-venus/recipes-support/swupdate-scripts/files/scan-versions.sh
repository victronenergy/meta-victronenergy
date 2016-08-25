#! /bin/sh

. $(dirname $0)/functions.sh

version=/opt/color-control/version
altroot=/mnt

get_version $version >/var/run/versions

other=$(get_altrootfs)

if [ -z "$other" ]; then
    echo "Unable to determine backup rootfs"
    exit 1
fi

lock || exit

if mount -r -t $rootfstype $(get_rootdev $other) $altroot; then
    get_version $altroot/$version >>/var/run/versions
    umount $altroot
fi

unlock
