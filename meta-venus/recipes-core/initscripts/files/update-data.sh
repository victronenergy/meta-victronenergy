#! /bin/sh

if [ ! -z "$1" ] && [ $1 = "-r" ]; then
    ARCHIVE_NAME=venus-runtime
else
    ARCHIVE_NAME=venus-data
fi
ARCHIVE_SUF="tar.gz tgz zip"

do_tar() {
    a=$1
    d=$2
    shift 2

    tar xzf "$a" -C "$d" "$@"
}

do_zip() {
    a=$1
    d=$2
    shift 2

    unzip -o -d "$d" "$a" "$@"
}

unpack() {
    archive=$1
    dest=$2

    case $archive in
        *.tar.gz|*.tgz)
            cmd=do_tar
            exclude=--exclude
            rc=rc
            ;;
        *.zip)
            cmd=do_zip
            exclude=-x
            rc="rc/*"
            ;;
    esac

    tmp=$(mktemp -d)
    trap 'rm -rf $tmp' EXIT

    pre="$tmp/rc/pre-hook.sh"
    post="$tmp/rc/post-hook.sh"

    $cmd "$archive" "$tmp" "$rc"

    if [ -f "$pre" ]; then
        sh "$pre" || return
    fi

    if $cmd "$archive" "$dest" $exclude "$rc"; then
        arg="success"
    else
        arg="extraction-failed"
    fi

    if [ -f "$post" ]; then
        sh "$post" "$arg"
    fi

    rm -rf $tmp
    trap - EXIT
}

update_data() {
    found=0
    for dir in /media/*; do
        for suf in $ARCHIVE_SUF; do
            for archive in $(ls $dir/$ARCHIVE_NAME-*.$suf $dir/$ARCHIVE_NAME.$suf 2> /dev/null); do
                echo "Updating /data with $archive"
                unpack "$archive" /data
                found=1
             done
        done
    done

    if [ $found -eq 1 ]; then
        return 0
    fi

    # Note: this triggers a delayed check, to support slow mounting media.
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
