rootfstype=ubifs

# print the number of the current rootfs
get_rootfs() {
    sed -n 's!^ubi.*:rootfs\([0-9]\) / .*!\1!p' /proc/mounts
}

# print device name of specified rootfs number
get_rootdev() {
    echo ubi0:rootfs$1
}

do_swupdate() {
    swupdate -b "0 1 2 3 4 5 6 7 8 9 10 11" "$@"
}
