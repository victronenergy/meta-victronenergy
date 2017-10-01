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

msg() {
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

    msg "Creating partitions..."
    sfdisk /dev/$mmc <<EOF
	2048, 16384, c, *
	, 655360, L
	, 655360, L
	,, E
	, 262144, L
	,, L
EOF

    msg "Formatting data partition..."
    mkfs.ext4 /dev/${mmc}p5

    msg "Formatting scratch partition.."
    mkfs.ext4 /dev/${mmc}p6

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

cleanup() {
    eval $HOOK_cleanup
    test -n "$UBIPART" && ubidetach -m $UBIPART
    umount ${CARD}
}

do_install() {
    do_mounts
    do_modules $MODULES
    waitdev $SWUDEV
    findimg $SWUDEV
    do_format
    setup_data $DATADEV $DATAFS
    install_swu

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
