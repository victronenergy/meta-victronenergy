#!/bin/bash
#
# check-updates.sh: wrapper script around swupdate
#
# Arguments:
# -auto    script will check automatic update setting in localsettings.
#          use this when calling from cron or after boot.
# -delay   sleep for a random delay before starting the download of
#          new image (to prevent thousands of units starting the
#          download at the same time).
#          use this when calling from cron or after boot.
# -check   (only) check if there is a new version available.
# -update  check and, when necessary, update.
# -force   force downloading and installing the new image, even if its
#          version is older or same as already installed version.
# -offline search for updates on removable storage devices
#
# Behaviour when called without any arguments is same as -update
#
#
# RESUME DOWNLOAD DETAILS
# swupdate can retry and resume a broken download. See -t and -r arguments
# in do_swupdate call at end of this file.
#
# Implementation in swupdate:
# https://github.com/sbabic/swupdate/blob/master/corelib/downloader.c
#
# Note that it only resumes the download when kept running: after the
# swupdate process has stopped, for example because of a reboot, it will
# restart. Improving this, without adding an intermediate scratchpad on disk,
# is not straightforward: the download is streamed straight into ubifs on the
# CCGX, and re-opening an unfinished ubifs volume is not a good idea.
#
# Resuming while online file has changed:
# When a new version is made available, the venus-swu-[machine].swu file on
# the webserver is replaced with the newer one. Devices busy starting a
# resume after that should not accidentally resume the download with the new
# file. A waste of bandwidth and possible leads to installing and booting
# into a corrupt rootfs. (What type of CRC or hash does swupdate on the swu
# file after download?)
#
# This is prevented this by dowloading the file that contains its version
# in the name. Therefore the webserver should always have the latest file
# available under two names:
# venus-swu-[machine].swu
# venus-swu-[machine]-[build-date-time].swu
#
# Best sequence of installing the files on the webserver is:
# 1. venus-swu-[machine]-[build-date-time].swu
# 2. venus-swu-[machine].swu
# 3. rename or remove the old build-date-time file: force the running
#    downloads to cancel.

. $(dirname $0)/functions.sh

get_setting() {
    dbus-send --print-reply=literal --system --type=method_call \
              --dest=com.victronenergy.settings \
              "/Settings/System/$1" \
              com.victronenergy.BusItem.GetValue |
        awk '{ print $3 }'
}

get_swu_version() {
    if [ -f "$1" ]; then
        # local file
        cmd="head -n 10"
    else
        # url, probably
        cmd="curl -s -r 0-999 -m 30 --retry 3"
    fi

    $cmd "$1" |
        cpio --quiet -i --to-stdout sw-description 2>/dev/null |
        sed -n '/venus-version/ {
            s/.*"\(.*\)".*/\1/
            p
            q
        }'
}

swu_status() {
    printf '%s\n%s\n' "$1" "$2" >$status_file
}

cleanup() {
    if [ "$need_umount" = y ]; then
        umount $dev
    fi
    rm -f "$named_pipe"
}

status_file=/var/run/swupdate-status

# location of named pipe
named_pipe=/var/tmp/check-updates

# create named pipe
# if it exists, an update is already in progress
mkfifo $named_pipe || exit

# remove pipe on the exit signal
trap cleanup EXIT

# start multilog process in background with stdin coming from named pipe
exec 3>&1
tee <$named_pipe /dev/fd/3 | multilog t s99999 n8 /log/swupdate &

# redirect stderr and stdout to named_pipe
exec 1>$named_pipe 2>&1

echo "*** Checking for updates ***"
echo "arguments: $@"

for arg; do
    case $arg in
        -auto)   update=auto ;;
        -check)  update=2    ;;
        -update) update=1    ;;
        -delay)  delay=y     ;;
        -force)  force=y     ;;
        -offline)offline=y   ;;
        *)       echo "Invalid option $arg"
                 exit 1
                 ;;
    esac
done

if [ "${update:-auto}" = auto ]; then
    update=$(get_setting AutoUpdate)
    case $update in
        0) echo "Auto-update disabled, exit."
           exit
           ;;
        1) ;;
        2) ;;
        *) echo "Invalid AutoUpdate value $update, exit."
           exit 1
           ;;
    esac
fi

if [ "$delay" = y ]; then
    DELAY=$[ $RANDOM % 3600 ]
    echo "Sleeping for $DELAY seconds"
    sleep $DELAY
fi

feed=$(get_setting ReleaseType)

case $feed in
    0) feed=release   ;;
    1) feed=candidate ;;
    2) feed=testing   ;;
    3) feed=develop   ;;
    *) echo "Invalid release type, exit."
       exit 1
       ;;
esac

machine=$(cat /etc/venus/machine)

URL_BASE=https://updates.victronenergy.com/feeds/venus/swu/${feed}
SWU=${URL_BASE}/venus-swu-${machine}.swu

if [ "$offline" = y ]; then
    echo "Searching for update on SD/USB..."

    for dev in /dev/mmcblk0p1 /dev/sd[a-z]1; do
        test -b $dev || continue
        if mount $dev /mnt 2>/dev/null; then
            # reverse order gives preference to an unversioned file
            # followed by that with the most recent timestamp if
            # multiple files exist
            SWU=$(ls -r /mnt/venus-swu-${machine}*.swu 2>/dev/null | head -n1)
            test -f "$SWU" && break
            umount $dev
        fi
    done

    if [ -f "$SWU" ]; then
        echo "Update found on $dev"
        need_umount=y
    else
        echo "Update not found. Exit."
        swu_status -1
        exit 1
    fi
fi

echo "Retrieving latest version (feed=$feed)..."
swu_status 1

cur_version=$(get_version)
swu_version=$(get_swu_version "$SWU")

if [ -z "$swu_version" ]; then
    echo "Unable to retrieve latest software version, exit."
    swu_status -1
    exit 1
fi

cur_build=${cur_version%% *}
swu_build=${swu_version%% *}

if [ "$offline" != y ]; then
    # change SWU url into the full name
    SWU=${URL_BASE}/venus-swu-${machine}-${swu_build}.swu
fi

echo "installed: $cur_version"
echo "available: $swu_version"

if [ "$force" != y -a "${swu_build}" -le "${cur_build}" ]; then
    echo "No newer version available, exit."
    swu_status 0
    exit
fi

if [ "$update" != 1 ]; then
    swu_status 0 "$swu_version"
    exit
fi

altroot=$(get_altrootfs)

if [ -z "$altroot" ]; then
    echo "Unable to determine rootfs. Exit."
    swu_status -2 "$swu_version"
    exit 1
fi

if ! lock; then
    echo "Can't get lock, other process already running? Exit."
    swu_status 0 "$swu_version"
    exit
fi

echo "Starting swupdate to install version $swu_version ..."
swu_status 2 "$swu_version"

# backup rootfs is about to be replaced, remove its version entry
get_version >/var/run/versions

if [ -f "$SWU" ]; then
    swupdate_flags="-i"
else
    swupdate_flags="-t 30 -r 3 -d"
fi

if do_swupdate $swupdate_flags "$SWU" -e "stable,copy$altroot"; then
    echo "do_swupdate completed OK. Rebooting"
    swu_status 3 "$swu_version"
    reboot
else
    echo "Error, do_swupdate stopped with exitcode $?, unlock and exit."
    swu_status -2 "$swu_version"
fi

unlock
