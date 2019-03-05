set -e

# mount point for installer medium
CARD=/mnt

# full path of swupdate image
SWU=${CARD}/venus.swu

# ttys to print progress/error message on
TTYS="console"

# devices to search for swupdate image
#SWUDEV=

# mmc device to install onto
#MMCDEV=

# mtd partition of ubi volumes
#UBIPART=

# modules to load
#MODULES=

# device/volume of /data, set by format_mmc/ubi
#DATADEV=

# filesystem type of /data, set by format_mmc/ubi
#DATAFS=

# set by findimg if ${CARD}/testmode exists
#TESTMODE=

# Most SOM with emmc seem to be at least 4GB, but the available size seem to be
# less then that, so by default reserve less then that.

# size of root partitions in MB
ROOT_SIZE=1280

# size of data partition in MB
DATA_SIZE=512

# name of board id nvmem device
#BOARD_ID_DEV=

# size of board id field in nvmem, padded with 0xff
BOARD_ID_SIZE=16

# file containing board id to program
BOARD_ID_FILE=${CARD}/board_id

msg() {
    eval "$HOOK_msg"
    for tty in $TTYS; do
        echo "$@" >/dev/$tty
    done
}

pause() {
    while :; do sleep 10; done
}

error() {
    test -n "$*" && msg "$*"
    msg "Error installing software"
    eval $HOOK_error
    pause
}

do_mounts() {
    test -e /proc/mounts && return

    mount -t proc none /proc
    mount -t sysfs none /sys
    grep -q ^devtmpfs /proc/mounts ||
        mount -t devtmpfs devtmpfs /dev

    mkdir -p /run/lock
}

do_modules() {
    for m; do
        modprobe $m
    done
}

do_mtdparts() {
    test -e /proc/mtd || return 0

    mkdir -p /dev/mtd

    for d in /sys/class/mtd/*; do
        test -e $d/name || continue
        dev=$(basename $d)
        name=$(cat $d/name)
        ln -s ../$dev /dev/mtd/$name
    done
}

waitdev() {
    while true; do
        for d; do
            test -e /dev/$d && return
        done
        sleep 1
    done
}

findimg() {
    msg "Searching for installer image..."

    for d; do
        if mount /dev/$d ${CARD}; then
            if [ -f ${SWU} ]; then
                DEV=$d
                break
            fi
            umount ${CARD}
        fi
    done

    if [ -f "${SWU}" ]; then
        msg "Installer image found on ${DEV}"
    else
        error "Installer image not found"
    fi

    if [ -f ${CARD}/testmode ]; then
        TESTMODE=1
        $HOOK_testmode
    fi
}

format_mmc() {
    mmc=$1

    root_blocks=$((ROOT_SIZE * 1024 * 2))
    data_blocks=$((DATA_SIZE * 1024 * 2))

    msg "Creating partitions..."
    sfdisk -W always /dev/$mmc <<EOF
	label: dos
	label-id: 0x564e5553
	2048, 16384, c, *
	, $root_blocks, L
	, $root_blocks, L
	,, E
	, $data_blocks, L
	,, L
EOF

    msg "Formatting data partition..."
    mkfs.ext4 -F /dev/${mmc}p5

    msg "Formatting scratch partition.."
    mkfs.ext4 -F /dev/${mmc}p6

    DATADEV=/dev/${mmc}p5
    DATAFS=ext4
}

format_ubi() {
    part=$1

    msg "Formatting UBI partition..."
    ubiformat --yes /dev/mtd$part
    ubiattach -m $part
    ubimkvol /dev/ubi0 -N rootfs1 -s 200MiB
    ubimkvol /dev/ubi0 -N rootfs2 -s 200MiB
    ubimkvol /dev/ubi0 -N data -s 32MiB

    DATADEV=ubi0:data
    DATAFS=ubifs
}

do_format() {
    eval $HOOK_format
    if [ -n "$MMCDEV" ]; then
        format_mmc $MMCDEV
    elif [ -n "$UBIPART" ]; then
        format_ubi $UBIPART
    fi
}

setup_data() {
    dev=$1
    fstype=$2

    msg "Creating /data/venus/installer-version..."
    mkdir -p /data
    mount -t $fstype $dev /data
    mkdir -p /data/venus
    cp /opt/victronenergy/version /data/venus/installer-version
    umount /data
}

ubiblacklist() {
    test -e /proc/mtd || return 0
    echo $(sed -n '/ubi/!s/^mtd\([0-9]*\):.*/\1/p' /proc/mtd)
}

install_swu() {
    set -- -i ${SWU}
    test -n "$UBIPART" && set -- "$@" -b "$(ubiblacklist)"

    msg "Installing rootfs1..."
    eval $HOOK_rootfs1
    swupdate "$@" -e "stable,copy1"

    msg "Installing rootfs2..."
    eval $HOOK_rootfs2
    swupdate "$@" -e "stable,copy2"

    msg "Installing bootloader..."
    eval $HOOK_bootloader
    swupdate "$@" -e "stable,bootloader"
}

read_board_id() {
    file=$1

    # strip newline, pad with 0xff
    (tr -d '\n' <$1; tr '\0' '\377' </dev/zero) |
        dd bs=1 count=$BOARD_ID_SIZE
}

setup_board_id() {
    test -n "$BOARD_ID_DEV" || return 0
    test -f $BOARD_ID_FILE|| return 0

    msg "Writing board ID..."

    board_id_mem=/sys/bus/nvmem/devices/$BOARD_ID_DEV/nvmem

    # get padded board id string
    board_id=$(read_board_id $BOARD_ID_FILE)

    # write to nvmem
    printf '%s' "$board_id" >$board_id_mem

    # read back and verify
    board_id_v=$(dd if=$board_id_mem bs=1 count=$BOARD_ID_SIZE)

    if [ "$board_id" != "$board_id_v" ]; then
        error "Failed to write board ID"
    fi
}

cleanup() {
    eval $HOOK_cleanup
    test -n "$UBIPART" && ubidetach -m $UBIPART
    umount ${CARD}
}

watchdog() (
    exec >/dev/watchdog
    while :; do
        echo t
        sleep 10
    done
)

do_install() {
    do_mounts
    do_modules $MODULES
    do_mtdparts
    watchdog &
    waitdev $SWUDEV
    findimg $SWUDEV
    do_format
    setup_data $DATADEV $DATAFS
    install_swu
    setup_board_id

    eval $HOOK_postinst

    if [ "$TESTMODE" = 1 ]; then
        msg "Enabling test mode..."
        fw_setenv runlevel 4
    fi

    cleanup

    msg "Installation complete"
    msg "Remove installer medium and power cycle system"
    pause
}

trap error EXIT
