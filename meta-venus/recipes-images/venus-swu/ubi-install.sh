#! /bin/sh

set -e

volume=$1
dev=/dev/$(basename $(dirname $(grep -lx $volume /sys/class/ubi/*/name)))

mnt=$(mktemp -d)

cleanup() {
    umount $mnt
    rmdir $mnt
}

trap cleanup EXIT

ubiupdatevol $dev -t
mount -t ubifs $dev $mnt
tar xz -C $mnt
