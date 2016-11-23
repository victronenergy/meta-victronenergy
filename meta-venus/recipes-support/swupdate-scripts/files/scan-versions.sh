#! /bin/sh

. $(dirname $0)/functions.sh

version=/opt/color-control/version
altroot=/mnt

echo "Active rootfs:" $(get_rootfs)

get_version $version | tee /var/run/versions | sed 's/^/Active version: /'

other=$(get_altrootfs)

if [ -z "$other" ]; then
    echo "Unable to determine backup rootfs"
    exit 1
fi

lock || exit

if mount -r -t $rootfstype $(get_rootdev $other) $altroot; then
    get_version $altroot/$version | tee -a /var/run/versions |
        sed 's/^/Backup version: /'
    umount $altroot
fi

unlock
