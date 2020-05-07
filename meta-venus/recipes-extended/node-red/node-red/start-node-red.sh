#!/bin/bash
exec 2>&1
echo "*** Starting node-red ***"

DATA_MODULES="/data/home/root/.node-red/node_modules"
VICTRON="$DATA_MODULES/@victronenergy"

if [ -d $VICTRON ]; then
	rm -rf $VICTRON
fi

if [ ! -f $VICTRON ]; then
    touch $VICTRON
fi

exec /usr/lib/node_modules/node-red/red.js
