#!/bin/sh

# make the rootfs writable and keep it writable after reboot
if ! awk '{ if ( $2 ~ "^/$" && $4 ~ /(^|,)ro($|,)/ ) { exit 1 } }' /proc/mounts; then
	echo "making the rootfs writable..."
	mount -o remount,rw /
	awk '$2~"^/$"{$4="defaults"}1' /etc/fstab > /etc/fstab.tmp && mv /etc/fstab.tmp /etc/fstab
fi
