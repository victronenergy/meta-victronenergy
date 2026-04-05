#!/bin/sh

# Older version ran as root, so move / change the owner of the files
# if they are in the homedir of root.
NODE_RED_ROOT="/data/home/root/.node-red"
NODE_RED="/data/home/nodered/.node-red"

if [ ! -d /data/home/nodered ]; then
    mkdir -p /data/home/nodered
    chown nodered:nodered /data/home/nodered
fi

if [ ! -d $NODE_RED ] && [ -d $NODE_RED_ROOT ]; then
	chown -R nodered:nodered $NODE_RED_ROOT
	mv $NODE_RED_ROOT $NODE_RED
	sync
fi

STALE_PKG="$NODE_RED/node_modules/victron-vrm-api"
if [ -d "$STALE_PKG" ]; then
	STALE_VERSION=$(node -e "console.log(require('$STALE_PKG/package.json').version)" 2>/dev/null || echo "unknown")
	echo "Removing stale local victron-vrm-api installation (version $STALE_VERSION)..."
	rm -rf "$STALE_PKG"
fi
