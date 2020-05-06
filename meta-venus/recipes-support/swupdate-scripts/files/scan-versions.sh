#! /bin/sh

. $(dirname $0)/functions.sh

version=/opt/victronenergy/version
altroot=/mnt

echo "Active rootfs:" $(get_rootfs)

get_version $version | tee /var/run/versions | sed 's/^/Active version: /'

other=$(get_altrootfs)

if [ -z "$other" ]; then
    echo "Unable to determine backup rootfs"
    exit 1
fi

lock || exit
trap unlock EXIT

altdev=$(get_rootdev $other)

case $rootfstype in
    ext*)
        if ! e2fsck -nf $altdev >/dev/null 2>&1; then
            echo "Filesystem errors detected on backup rootfs"
            exit 1
        fi
    ;;
esac

if mount -r -t $rootfstype $altdev $altroot; then
    get_version $altroot/$version | tee -a /var/run/versions |
        sed 's/^/Backup version: /'
    umount $altroot
fi
