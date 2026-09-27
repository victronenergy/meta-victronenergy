#!/bin/sh

DEVICE=/dev/mmcblk0
ROOT_SIZE_MIB=4096

mount -o remount,rw /

partition_table=$(parted "$DEVICE" -ms unit s print)
LAST_PART_NUM=$(echo "$partition_table" | tail -n 1 | cut -f 1 -d:)

if [ $LAST_PART_NUM -ne 2 ]; then
	echo "more then two partions already exist"
	exit 0
fi

sector_size=$(cat /sys/class/block/mmcblk0/queue/logical_block_size)
device_sectors=$(echo "$partition_table" | awk -F: '/^\/dev\// { sub(/s$/, "", $2); print $2 }')
root_a_start=$(echo "$partition_table" | awk -F: '$1 == "2" { sub(/s$/, "", $2); print $2 }')
root_size_sectors=$((ROOT_SIZE_MIB * 1024 * 1024 / sector_size))
root_a_end=$((root_a_start + root_size_sectors - 1))
root_b_start=$((root_a_end + 1))
root_b_end=$((root_b_start + root_size_sectors - 1))
data_start=$((root_b_end + 1))

if [ -z "$root_a_start" ] || [ "$data_start" -ge "$device_sectors" ]; then
	echo "sd card is too small"
	exit 1
fi

echo "MIND IT: CHANGING THE MBR!!!"
parted "$DEVICE" -ms unit s \
	resizepart 2 "${root_a_end}s" \
	mkpart primary ext4 "${root_b_start}s" "${root_b_end}s" \
	mkpart primary ext4 "${data_start}s" 100%
echo "DONE!!!"

mkfs.ext4 -F /dev/mmcblk0p3
mkfs.ext4 -F /dev/mmcblk0p4

update-rc.d -f zzz-resize-sdcard remove

# Create /data/venus/installer-version
mkdir -p /data
mount -t ext4 /dev/mmcblk0p4 /data
mkdir -p /data/venus
cp /opt/victronenergy/version /data/venus/installer-version

