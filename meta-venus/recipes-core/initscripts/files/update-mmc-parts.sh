#! /bin/sh

set -e

DEV=/dev/mmcblk1

finish() {
    echo "$@"
    exit 0
}

# Exit early if partition size is not as expected, e.g. if the
# partition layout has already been updated.
if [ $(sfdisk -s ${DEV}p2) != 327680 ]; then
    finish "Partition table already up to date"
fi

mount /var/volatile
mkdir -p /var/volatile/tmp

# Make sure the exiting partition layout is exactly as expected.
# Ignore the preamble as the disk id might be a random value.
# Also ignore the size of the last partition as the mmc size varies.
tmp=$(mktemp)
sfdisk -d $DEV | grep ^/ | sed '/p[46] :/s/size=[ 0-9]*/size=/' >$tmp
cmp $tmp - <<EOF || finish "Unexpected partition table"
/dev/mmcblk1p1 : start=        2048, size=       16384, type=c, bootable
/dev/mmcblk1p2 : start=       18432, size=      655360, type=83
/dev/mmcblk1p3 : start=      673792, size=      655360, type=83
/dev/mmcblk1p4 : start=     1329152, size=, type=5
/dev/mmcblk1p5 : start=     1331200, size=      262144, type=83
/dev/mmcblk1p6 : start=     1595392, size=, type=83
EOF

# Make sure rootfs is read-only so it can be relocated safely.
echo "Remounting / read-only..."
mount -o remount,ro /

# New size of rootfs in 512-byte sectors
ROOT_SIZE=$((1280 * 1024 * 2))

# New partition start sectors
ROOTFS1_START=$((2048 + 16384))
ROOTFS2_START=$((ROOTFS1_START + ROOT_SIZE))
EXTPART_START=$((ROOTFS2_START + ROOT_SIZE))
DATA_START=$((EXTPART_START + 2048))

# Start of old scratch partition
P6_START=1595392

# Unmount /data if mounted
if [ $(stat -c %D /data) != $(stat -c %D /) ]; then
    echo "Unmounting /data..."
    umount /data
fi

# Copying partitions can take a while, so pet the watchdog. Since the
# normal watchdog daemon performs various checks we do not want here,
# use a simple loop instead.

watchdog() {
    exec >/dev/watchdog
    while :; do
        echo t
        sleep 10
    done
}

watchdog &

# Copy the rootfs2 and data partitions to their new locations. Use
# partition 6 as the destination to avoid accidentally overwriting
# existing data. This also avoids a problem in busybox dd when the
# seek offset (in bytes) overflows 32 bits.

echo "Copying rootfs2..."
dd if=${DEV}p3 of=${DEV}p6 bs=512 seek=$((ROOTFS2_START - P6_START))

echo "Copying data..."
dd if=${DEV}p5 of=${DEV}p6 bs=512 seek=$((DATA_START - P6_START))

sync

# Done copying, stop petting the dog in case something odd happens.
kill $!

echo "Writing new partition table..."
sfdisk -W never -w never --no-reread --no-tell-kernel $DEV <<EOF
label: dos
label-id: 0x564e5553
2048, 16384, c, *
, $ROOT_SIZE, L
, $ROOT_SIZE, L
,, E
,, L
EOF

sync

# Reboot immediately
echo "Rebooting..."
reboot -f
sleep 10
