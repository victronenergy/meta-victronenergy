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
            unzip -d "$dest" "$archive"
            ;;
    esac
}

for dir in /media/*; do
    for suf in $ARCHIVE_SUF; do
        archive=$dir/$ARCHIVE_NAME.$suf
        if [ -f "$archive" ]; then
            echo "Updating /data with $archive"
            unpack "$archive" /data
        fi
    done
done
