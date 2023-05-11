#!/bin/bash

CONF="/data/conf/venus-server"

if [ ! -d $CONF ]; then
    mkdir -p $CONF
fi

# Note: -h is needed to prevent errors when trying to change dangling symlinks
chown -Rh vserver:vserver $CONF

DEFAULTCONF="/usr/lib/node_modules/venus-server/defaults"

if [ ! -f $CONF/config.json ]; then
    cp "$DEFAULTCONF/config.json" "$CONF"
fi

