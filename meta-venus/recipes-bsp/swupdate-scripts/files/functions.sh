# print the version and timestamp as one line
# optional argument overrides version file
get_version() {
    sed -n -e '1h3{Gs/\n/ /p}' "${1:-/opt/color-control/version}"
}

# print the number of the current rootfs
get_rootfs() {
    sed -n 's!^ubi.*:rootfs\([0-9]\) / .*!\1!p' /proc/mounts
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
