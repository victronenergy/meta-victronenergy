#!/bin/sh

# make the rootfs writable and keep it writable after reboot
if ! awk '{ if ( $2 ~ "^/$" && $4 ~ /(^|,)ro($|,)/ ) { exit 1 } }' /proc/mounts; then
	echo "making the rootfs writable..."
	mount -o remount,rw /
	# MIND IT, the weird cat below is because the busybox awk doesn't check if it
	# can write to stdout, it will simply fail successfully. This can result in ending
	# up with an empty fstab and hence a bricked device. So keep the cat or fix busybox
	# or require gawk...
	awk '$2~"^/$"{$4="defaults"}1' /etc/fstab | cat > /etc/fstab.tmp && sync && mv /etc/fstab.tmp /etc/fstab && sync
fi
