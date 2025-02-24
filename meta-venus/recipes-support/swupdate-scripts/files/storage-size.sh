#!/bin/sh

# only block devices are supported at the moment
rootpart="$(mount | grep ' on / ' | awk '{print $1}')"
parentdev=$(echo $rootpart | sed -n -r 's,^/dev/([a-zA-Z0-9]+)p[0-9]$,\1,p')
if [ "$parentdev" = "" ]; then
    echo "$rootdev doesn't seem to be a block device" 1>&2
    exit 1
fi

sysdir="/sys/block/$parentdev"
if [ ! -d $sysdir ]; then
    echo "$sysdir doesn't exist" 1>&2
    exit 1
fi

awk '{print $1*512}' "$sysdir/size"

