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
    echo /dev/mmcblk1p$(($1 + 1))
}

do_swupdate() {
    echo 0 >/sys/block/mmcblk1boot1/force_ro
    swupdate "$@"
}
