rootfstype=ext4

# print the number of the current rootfs
get_rootfs() {
    dev=$(stat -c %d /)
    case $((dev & 7)) in
        2) echo 1 ;;
        3) echo 2 ;;
    esac
}

# print device name of specified rootfs number
get_rootdev() {
    echo /dev/mmcblk0p$(($1 + 1))
}

do_swupdate() {
    unlock_env
    swupdate "$@"
}

unlock_env() {
    : # nothing to do
}
