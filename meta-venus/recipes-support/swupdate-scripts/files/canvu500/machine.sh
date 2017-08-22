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
    blacklist=$(sed -n '/ubi/!s/^mtd\([0-9]*\):.*/\1/p' /proc/mtd)
    blacklist=$(echo $blacklist) # make space-separated list
    swupdate -b "$blacklist" "$@"
}

unlock_env() {
    : # nothing to do
}
