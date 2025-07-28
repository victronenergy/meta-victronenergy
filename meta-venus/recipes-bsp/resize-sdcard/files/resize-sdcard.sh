#!/bin/sh

mount -o remount,rw /

LAST_PART_NUM=$(parted /dev/mmcblk0 -ms unit s p | tail -n 1 | cut -f 1 -d:)

if [ $LAST_PART_NUM -ne 2 ]; then
	echo "more then two partions already exist"
	exit 0
fi

size=$(cat /sys/class/block/mmcblk0/size)

if [ $size -lt 1562500 ]; then
	echo "sd card is too small"
	exit 1
fi

echo "MIND IT: CHANGING THE MBR!!!"
parted /dev/mmcblk0 -ms resizepart 2 33% mkpart primary ext4 33% 66% mkpart primary ext4 66% 100%
echo "DONE!!!"

mkfs.ext4 -F /dev/mmcblk0p3
mkfs.ext4 -F /dev/mmcblk0p4

update-rc.d -f zzz-resize-sdcard remove

# Create /data/venus/installer-version
mkdir -p /data
mount -t ext4 /dev/mmcblk0p4 /data
mkdir -p /data/venus
cp /opt/victronenergy/version /data/venus/installer-version

