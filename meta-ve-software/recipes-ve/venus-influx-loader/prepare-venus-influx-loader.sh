#!/bin/bash

CONF="/data/conf/venus-influx-loader"

if [ ! -d $CONF ]; then
    mkdir -p $CONF
fi

# Note: -h is needed to prevent errors when trying to change dangling symlinks
chown -Rh vinfluxl:vinfluxl $CONF

DEFAULTCONF="/usr/lib/node_modules/venus-influx-loader/defaults"

if [ ! -f $CONF/config.json ]; then
    cp "$DEFAULTCONF/config.json" "$CONF"
fi

