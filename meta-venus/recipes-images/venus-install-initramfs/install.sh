set -e

# mount point for installer medium
CARD=/mnt

# full path of swupdate image
SWU=${CARD}/venus.swu

# ttys to print progress/error message on
TTYS="console"

# additional ttys for test mode
#TESTMODE_TTYS=

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

# set if ${CARD}/testmode exists
#TESTMODE=

# Most SOM with emmc seem to be at least 4GB, but the available size seem to be
# less then that, so by default reserve less then that.

# default root partition size for mmc devices up to 4 GB
SMALL_ROOT_SIZE=1280

# default root partition size for mmc devices larger than 4 GB
LARGE_ROOT_SIZE=2048

# size of root partitions in MB
ROOT_SIZE=$SMALL_ROOT_SIZE

# name of board id nvmem device
#BOARD_ID_DEV=

# size of board id field in nvmem, padded with 0xff
BOARD_ID_SIZE=16

# file containing board id to program
BOARD_ID_FILE=${CARD}/board_id

# name of edid nvmem device
#EDID_DEV=

# file containing edid to program
EDID_FILE=${CARD}/edid.bin

# location of firmware files
FIRMWARE_DIR=/opt/victronenergy/firmware

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
}

do_testmode() {
    test -f ${CARD}/testmode || return 0

    TESTMODE=1
    eval $HOOK_testmode

    for tty in $TESTMODE_TTYS; do
        test -c /dev/$tty || continue
        stty -F /dev/$tty 115200
        TTYS="$TTYS $tty"
    done
}

format_mmc() {
    mmc=$1

    if [ -z "$ROOT_SIZE" ]; then
        mmc_size=$(cat /sys/block/$mmc/size)
        if [ $mmc_size -gt $((4 * 1024 * 1024 * 2)) ]; then
            ROOT_SIZE=$LARGE_ROOT_SIZE
        else
            ROOT_SIZE=$SMALL_ROOT_SIZE
        fi
    fi

    root_blocks=$((ROOT_SIZE * 1024 * 2))

    msg "Creating partitions..."
    sfdisk -W always /dev/$mmc <<EOF
	label: dos
	label-id: 0x564e5553
	2048, 16384, c, *
	, $root_blocks, L
	, $root_blocks, L
	,, E
	,, L
EOF

    msg "Formatting data partition..."
    mkfs.ext4 -F /dev/${mmc}p5

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
    board=$(tr '\0' '\n' </sys/firmware/devicetree/base/compatible | head -n1)

    set -- -v -H "venus:${board}" -i ${SWU}
    test -n "$UBIPART" && set -- "$@" -b "$(ubiblacklist)"

    msg "Installing rootfs1..."
    eval $HOOK_rootfs1
    swupdate "$@" -e "stable,copy1"

    msg "Installing bootloader..."
    eval $HOOK_bootloader
    swupdate "$@" -e "stable,bootloader"
}

install_firmware() {
    test -d "$FIRMWARE_DIR" || return 0

    msg "Installing firmware..."

    find $FIRMWARE_DIR -type f | while read f; do
        /opt/victronenergy/venus-firmware-update/venus-firmware-update "$f"
    done
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

setup_edid() {
    test -n "$EDID_DEV" || return 0
    test -f "$EDID_FILE" || return 0

    msg "Writing EDID..."

    edid_mem=/sys/bus/nvmem/devices/$EDID_DEV/nvmem
    edid_size=$(stat -c %s $EDID_FILE)

    # write to nvmem
    dd if=$EDID_FILE of=$edid_mem bs=16

    # read back and verify
    dd if=$edid_mem of=/tmp/edid.bin bs=1 count=$edid_size

    if ! cmp -s $EDID_FILE /tmp/edid.bin; then
        error "Failed to write EDID"
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
    do_testmode
    do_format
    setup_data $DATADEV $DATAFS
    install_swu
    install_firmware
    setup_board_id
    setup_edid

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
