. $(dirname $0)/machine.sh

# print the version and timestamp as one line
# optional argument overrides version file
get_version() {
    sed -n -e '1h;3{G;s/\n/ /p}' "${1:-/opt/victronenergy/version}"
}

# print the number of the backup rootfs
get_altrootfs() {
    rootfs=$(get_rootfs)
    case $rootfs in
        1) echo 2 ;;
        2) echo 1 ;;
    esac
}

lock_file=/var/lock/swupdate

# create lock file, return error if it already exists
lock() {
    ln -s $$ "$lock_file"
}

# delete lock file if it belongs to this process
unlock() {
    test "$(readlink "$lock_file")" = $$ && rm -f "$lock_file"
}

# start background logging of stdout and stderr
start_log() {
    log_pipe=/var/tmp/$(basename $0).log
    mkfifo $log_pipe || return
    exec 3>&1
    tee <$log_pipe /dev/fd/3 | multilog t s99999 n8 /data/log/swupdate &
    exec 1>$log_pipe 2>&1
    rm -f $log_pipe
}
