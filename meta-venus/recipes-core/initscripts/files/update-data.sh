#! /bin/sh

ARCHIVE_NAME=venus-data
ARCHIVE_SUF="tar.gz tgz zip"

unpack() {
    archive=$1
    dest=$2

    case $archive in
        *.tar.gz|*.tgz)
            tar xzf "$archive" -C "$dest"
            ;;
        *.zip)
            unzip -o -d "$dest" "$archive"
            ;;
    esac
}

update_data() {
    for dir in /media/*; do
        for suf in $ARCHIVE_SUF; do
            archive=$dir/$ARCHIVE_NAME.$suf
            if [ -f "$archive" ]; then
                echo "Updating /data with $archive"
                unpack "$archive" /data && return 0
            fi
        done
    done

    return 1
}

delayed_update() {
    udevadm settle
    mkdir -p $(readlink /media)
    inotifywait -q -m -e create,isdir -t 30 /media
    update_data
}

update_data && exit 0
delayed_update &
