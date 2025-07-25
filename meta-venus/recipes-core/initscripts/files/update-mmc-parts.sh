#! /bin/sh

# This script resizes the partitions to make better use of 8 GB MMC
# devices.  The old layout can be either "small" or "large" as shown
# below.  Both are converted to the "new" layout.
#
# Systems with the "large" layout must be running from rootfs1 since
# the old and new locations of rootfs2 overlap.
#
# small         large         new
# +---- 2048 +  +---- 2048 +  +---- 2048 +
# |   unused |  |   unused |  |   unused |
# +----------+  +----------+  +----------+
# +--- 18432 +  +--- 18432 +  +--- 18432 +
# |  rootfs1 |  |  rootfs1 |  |  rootfs1 |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# +----------+  |          |  |          |
# +- 2639872 +  |          |  |          |
# |  rootfs2 |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  +----------+  |          |
# |          |  +- 4212736 +  |          |
# |          |  |  rootfs2 |  |          |
# |          |  |          |  |          |
# +----------+  |          |  |          |
# +- 5261312 +  |          |  |          |
# |     data |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  +----------+
# |          |  |          |  +- 4212736 +
# |          |  |          |  |  rootfs2 |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  +----------+  |          |
# |          |  +- 8407040 +  |          |
# |          |  |     data |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  +----------+
# |          |  |          |  +- 8407040 +
# |          |  |          |  |     data |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# |          |  |          |  |          |
# +----------+  +----------+  +----------+

set -e
set -u

DEV=/dev/mmcblk1
DEVSIZE=$(sfdisk -s $DEV)

finish() {
    echo "$@"
    exit 0
}

if [ $DEVSIZE -lt $((4 * 1024 * 1024)) ]; then
    finish "Small MMC, unable to resize"
fi

# New size of rootfs in 1k blocks
ROOT_SIZE_NEW=$((3125 * 1024))

# Old size of rootfs in 1k blocks
ROOT_SIZE_OLD=$(sfdisk -s ${DEV}p2)

# Allowed old rootfs sizes in 1k blocks
SMALL_ROOT_OLD=$((1280 * 1024))
LARGE_ROOT_OLD=$((2048 * 1024))

# Exit early if partition size is not as expected, e.g. if the
# partition layout has already been updated.
case $ROOT_SIZE_OLD in
    $SMALL_ROOT_OLD)
        DEVTYPE=small
        ;;
    $LARGE_ROOT_OLD)
        DEVTYPE=large
        ;;
    $ROOT_SIZE_NEW)
        DEVTYPE=new
        ;;
    *)
        finish "Unexpected rootfs size"
        ;;
esac

echo "Detected $DEVTYPE rootfs"

if [ $DEVTYPE = new ]; then
    DATA_BLOCKS=$(dumpe2fs -h ${DEV}p5 2>/dev/null | sed -n '/^Block \(count\|size\):/s/.*: *//p')

    if [ $(dc -e "$DATA_BLOCKS*p") -gt $((1024 * 1024 * 1024)) ]; then
        finish "Nothing to do"
    fi

    echo "Expanding data filesystem..."
    resize2fs ${DEV}p5
    finish "Repartitioning complete"
fi

ROOTDEV=$(stat -c %d /)

if [ $DEVTYPE = large ] && [ $((ROOTDEV & 255)) != 2 ]; then
    finish "Resizing possible only when running from rootfs1"
fi

mount /var/volatile
mkdir -p /var/volatile/tmp

# Make sure the exiting partition layout is exactly as expected.
# Ignore the preamble as the disk id might be a random value.
# Also ignore the size of the last partition to allow for some
# variation in device size.

check_parts_small() {
    cmp $1 - <<EOF
/dev/mmcblk1p1 : start=        2048, size=       16384, type=c, bootable
/dev/mmcblk1p2 : start=       18432, size=     2621440, type=83
/dev/mmcblk1p3 : start=     2639872, size=     2621440, type=83
/dev/mmcblk1p4 : start=     5261312, , type=5
/dev/mmcblk1p5 : start=     5263360, , type=83
EOF
}

check_parts_large() {
    cmp $1 - <<EOF
/dev/mmcblk1p1 : start=        2048, size=       16384, type=c, bootable
/dev/mmcblk1p2 : start=       18432, size=     4194304, type=83
/dev/mmcblk1p3 : start=     4212736, size=     4194304, type=83
/dev/mmcblk1p4 : start=     8407040, , type=5
/dev/mmcblk1p5 : start=     8409088, , type=83
EOF
}

tmp=$(mktemp)
trap "rm $tmp" EXIT

sfdisk -d $DEV | sed -n -e '/type=5/,$s/size= *[0-9]*//' -e '/^\//p' >$tmp
check_parts_$DEVTYPE $tmp || finish "Unexpected partition table"

# Make sure rootfs is read-only so it can be relocated safely.
echo "Remounting / read-only..."
mount -o remount,ro /

# New size of rootfs in 512-byte sectors
ROOT_SIZE=$((3125 * 1024 * 2))

# New partition start sectors
ROOTFS1_START=$((2048 + 16384))
ROOTFS2_START=$((ROOTFS1_START + ROOT_SIZE))
EXTPART_START=$((ROOTFS2_START + ROOT_SIZE))
DATA_START=$((EXTPART_START + 2048))

# Unmount /data if mounted
if [ $(stat -c %d /data) != $ROOTDEV ]; then
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

# Shrink the data filesystem and copy it to the new location. Make it
# a little smaller than the expected size of the partition and expand
# it after rebooting. If the shrinking operation fails, e.g. because
# the filesystem is too full, the script will abort.

# Temporary size of data filesystem in 512-byte sectors
DATA_SIZE=$((1024 * 1024 * 2))

echo "Resizing data filesystem..."
e2fsck -f -p ${DEV}p5
resize2fs ${DEV}p5 ${DATA_SIZE}s

echo "Copying data..."
dd if=${DEV}p5 of=${DEV} bs=512 seek=$DATA_START count=$DATA_SIZE

# The new rootfs2 partition might overlap the old one. In this case,
# copy the overlapping part first.

get_start() {
    sfdisk -d ${1%p*} | sed -n "\\:^$1:"'s/^.*start= *\([0-9]*\),.*/\1/p'
}

EXT_OLD=$(get_start ${DEV}p4)

echo "Copying rootfs2..."
if [ $ROOTFS2_START -lt $EXT_OLD ]; then
    SPLIT=$((EXT_OLD - ROOTFS2_START))
    dd if=${DEV}p3 of=${DEV} bs=512 seek=$((ROOTFS2_START + SPLIT)) skip=$SPLIT
    dd if=${DEV}p3 of=${DEV} bs=512 seek=$ROOTFS2_START count=$SPLIT
else
    dd if=${DEV}p3 of=${DEV} bs=512 seek=$ROOTFS2_START
fi

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
