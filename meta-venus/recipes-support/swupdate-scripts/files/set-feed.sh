#!/bin/sh

set -e

$(dirname "$0")/set-swu-feed.sh $1
$(dirname "$0")/resize2fs.sh

echo "Switching opkg feed to $1"
ln -sf /usr/share/venus-feed-configs/opkg-$1.conf /etc/opkg/venus.conf
