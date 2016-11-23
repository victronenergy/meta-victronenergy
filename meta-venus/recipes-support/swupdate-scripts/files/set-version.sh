#! /bin/sh

. $(dirname $0)/functions.sh

start_log

case $1 in
    1) exit # no change
       ;;
    2) version=$(get_altrootfs)
       ;;
    *) echo "Invalid version $1"
       exit 1
       ;;
esac

if [ -z "$version" ]; then
    echo "Unable to determine new version"
    exit 1
fi

lock || exit

echo "switching to rootfs $version"
sed '1s/^/current version: /;2s/^/new version: /' /var/run/versions

unlock_env
fw_setenv version $version
reboot
unlock
