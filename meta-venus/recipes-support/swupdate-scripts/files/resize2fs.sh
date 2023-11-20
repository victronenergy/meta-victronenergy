#!/bin/sh

. $(dirname $0)/functions.sh

root=$(get_rootdev $(get_rootfs))
if [ -z "$root" ]; then
    echo "Unable to determine rootfs. Exit."
    exit 1
fi

# make the rootfs writable and keep it writable after reboot

if ! awk '{ if ( $2 ~ "^/$" && $4 ~ /(^|,)ro($|,)/ ) { exit 1 } }' /proc/mounts; then
	was_ro="1"
    echo "making the rootfs writable..."
    mount -o remount,rw /
fi

fs=$(grep -E '^\S+\s+/\s' /proc/mounts | awk '{print $3}')
if [ "$fs" == "ext4" ]; then
    echo "resizing $root"
    resize2fs $root
else
    echo "Rootfs is not ext4, but $fs"
fi

if [ "$was_ro" = "1" ]; then
	# MIND IT, the weird cat below is because the busybox awk doesn't check if it
	# can write to stdout, it will simply fail successfully. This can result in ending
	# up with an empty fstab and hence a bricked device. So keep the cat or fix busybox
	# or require gawk...
	echo "modifying /etc/fstab"
	awk '$2~"^/$"{$4="defaults"}1' /etc/fstab | cat > /etc/fstab.tmp && sync && mv /etc/fstab.tmp /etc/fstab && sync
fi

