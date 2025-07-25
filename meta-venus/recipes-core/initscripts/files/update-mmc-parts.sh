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
# |          |  |          |  +- 6418432 +
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
# |          |  |          |  + 12818432 +
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

STATE_ENV=update_mmc_parts

get_state() {
    fw_printenv -n -l /tmp $STATE_ENV 2>/dev/null || true
}

set_state() {
    fw_setenv -l /tmp $STATE_ENV "$@"
}

# Print a message and exit
finish() {
    echo "$@"
    exit 0
}

# Set state variable and exit with message
error() {
    err=$1
    shift
    set_state $err
    finish "$@"
}

# Find the start sector of a partition
partstart() {
    sfdisk -d ${1%p*} | sed -n "\\:^$1:"'s/^.*start= *\([0-9]*\),.*/\1/p'
}

# Get the size of a filesystem in 512-byte sectors
fssize() {
    dumpe2fs -h $1 2>/dev/null |
        sed -n '/^Block \(count\|size\):/s/.*: *//p' |
        dc -f /proc/self/fd/0 -e '*512/p'
}

# Some of the commands used require these to be mounted
mount /proc
mount /var/volatile
mkdir -p /var/volatile/tmp

STATE=$(get_state)

case $STATE in
    0) finish "Partition update not requested" ;;
    1) echo "Partition update requested / in progress" ;;
    2) finish "Partition update complete" ;;
    3) finish "Partition update failed" ;;
    4) finish "Partition update not supported" ;;
    '') CHECK=1 ;;              # First boot
    *) finish "Unexpected value of $STATE_ENV: $STATE" ;;
esac

# Device name
DEV=/dev/mmcblk1
R1DEV=${DEV}p2
R2DEV=${DEV}p3
DATADEV=${DEV}p5

# Size of MMC device
DEVSIZE=$(sfdisk -s $DEV)

if [ $DEVSIZE -lt $((4 * 1024 * 1024)) ]; then
    error 4 "Small MMC, unable to resize"
fi

# New size of rootfs in 1k blocks
ROOT_SIZE_NEW=$((3125 * 1024))

# Old size of rootfs in 1k blocks
ROOT_SIZE_OLD=$(sfdisk -s $R1DEV)

# Allowed old rootfs sizes in 1k blocks
SMALL_ROOT_OLD=$((1280 * 1024))
LARGE_ROOT_OLD=$((2048 * 1024))

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
        error 4 "Unexpected rootfs size"
        ;;
esac

if [ $DEVTYPE != new ] && [ $(partstart $DATADEV) -gt 10000000 ]; then
    STAGE=_stage1
else
    STAGE=
fi

echo "Detected ${DEVTYPE}${STAGE} rootfs"

DISKID=0x564e5553               # VNUS
DISKID_STAGE1=0x766e7573        # vnus

if [ $(sfdisk --disk-id $DEV) = $DISKID_STAGE1 ]; then
    # Found stage1 disk ID. This means the data partition has been
    # moved. Expand the filesystem, then set the disk ID back to
    # normal and exit, allowing system startup to proceed. The
    # resizing of rootfs partitions will resume on the next boot,
    # scheduled with a delay to allow normal system operation for a
    # few minutes.

    echo "Expanding data filesystem..."
    e2fsck -f -p $DATADEV
    resize2fs $DATADEV

    sfdisk --no-reread --no-tell-kernel --disk-id $DEV $DISKID
    sleep 600 && reboot &

    finish "Booting stage1..."
fi

if [ $DEVTYPE = new ]; then
    # First boot or repartitioning has been requested through the
    # u-boot variable on a device that doesn't need it.
    set_state 2
    finish "New layout in use, nothing to do"
fi

# Make sure the existing partition layout is exactly as expected.
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

check_parts_small_stage1() {
    cmp $1 - <<EOF
/dev/mmcblk1p1 : start=        2048, size=       16384, type=c, bootable
/dev/mmcblk1p2 : start=       18432, size=     2621440, type=83
/dev/mmcblk1p3 : start=     2639872, size=     2621440, type=83
/dev/mmcblk1p4 : start=     5261312, , type=5
/dev/mmcblk1p5 : start=    12820480, , type=83
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

check_parts_large_stage1() {
    cmp $1 - <<EOF
/dev/mmcblk1p1 : start=        2048, size=       16384, type=c, bootable
/dev/mmcblk1p2 : start=       18432, size=     4194304, type=83
/dev/mmcblk1p3 : start=     4212736, size=     4194304, type=83
/dev/mmcblk1p4 : start=     8407040, , type=5
/dev/mmcblk1p5 : start=    12820480, , type=83
EOF
}

tmp=$(mktemp)
trap "rm $tmp" EXIT

sfdisk -d $DEV | sed -n -e '/type=5/,$s/size= *[0-9]*//' -e '/^\//p' >$tmp
check_parts_${DEVTYPE}${STAGE} $tmp || error 4 "Unexpected partition table"

if [ ${CHECK:-0} = 1 ]; then
    # Update is possible, set u-boot env to indicate this and exit.
    set_state 0
    exit 0
fi

ROOTDEV=$(stat -c %d /)
ROOTPART=$((ROOTDEV & 255))

if [ $DEVTYPE = large ] && [ $ROOTPART != 2 ]; then
    finish "Resizing possible only when running from rootfs1"
fi

# Make sure rootfs is read-only so it can be relocated safely.
echo "Remounting / read-only..."
mount -o remount,ro /

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

watchdog_start() {
    watchdog &
    WATCHDOG_PID=$!
}

watchdog_stop() {
    kill $WATCHDOG_PID
}

# Reboot immediately
restart() {
    echo "Rebooting..."
    sync
    reboot -f
    sleep 10
}

# New size of rootfs in 512-byte sectors
ROOT_SIZE=$((3125 * 1024 * 2))

# New partition start sectors
ROOTFS1_START=$((2048 + 16384))
ROOTFS2_START=$((ROOTFS1_START + ROOT_SIZE))
EXTPART_START=$((ROOTFS2_START + ROOT_SIZE))
DATA_START=$((EXTPART_START + 2048))

if [ -z "$STAGE" ]; then
    # Shrink the data filesystem and copy it to the new location. Make
    # it as small as possible and expand it again after rebooting.

    echo "Resizing data filesystem..."
    e2fsck -f -p $DATADEV
    resize2fs -M $DATADEV

    # Temporary size of data filesystem in 512-byte sectors
    DATA_SIZE=$(fssize $DATADEV)

    if [ $DATA_SIZE -gt $((1024 * 1024 * 2)) ]; then
        error 3 "Data filesystem too full"
    fi

    watchdog_start

    echo "Copying data..."
    dd if=$DATADEV of=$DEV bs=512 seek=$DATA_START count=$DATA_SIZE
    sync

    watchdog_stop

    ROOT_SIZE_S1=$((ROOT_SIZE_OLD * 2))

    echo "Writing stage1 partition table..."
    sfdisk -W never -w never --no-reread --no-tell-kernel $DEV <<EOF
label: dos
label-id: $DISKID_STAGE1
2048, 16384, c, *
, $ROOT_SIZE_S1, L
, $ROOT_SIZE_S1, L
,, E
$DATA_START,, L
EOF

    restart
fi

# If we get here, the data partition has been moved. Proceed with
# moving rootfs2 and writing the final partition table.

watchdog_start

echo "Copying rootfs2..."

# The new rootfs2 partition might overlap the old one. In this case,
# copy the overlapping part first.

OLD_START=$(partstart $R2DEV)
OLD_SIZE=$(fssize $R2DEV)
OLD_END=$((OLD_START + OLD_SIZE))

if [ $ROOTFS2_START -lt $OLD_END ]; then
    SPLIT1=$((ROOTFS2_START - OLD_START))
    SPLIT2=$((OLD_END - ROOTFS2_START))
    dd if=$R2DEV of=$DEV bs=512 seek=$((ROOTFS2_START + SPLIT1)) skip=$SPLIT1 count=$SPLIT2
    dd if=$R2DEV of=$DEV bs=512 seek=$ROOTFS2_START count=$SPLIT1
else
    dd if=$R2DEV of=$DEV bs=512 seek=$ROOTFS2_START count=$OLD_SIZE
fi

sync

watchdog_stop

echo "Writing new partition table..."
sfdisk -W never -w never --no-reread --no-tell-kernel $DEV <<EOF
label: dos
label-id: $DISKID
2048, 16384, c, *
, $ROOT_SIZE, L
, $ROOT_SIZE, L
,, E
,, L
EOF

set_state 2
restart
