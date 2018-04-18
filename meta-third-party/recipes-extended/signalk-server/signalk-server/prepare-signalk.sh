#!/bin/bash

CONF="/data/conf/signalk"

if [ ! -d $CONF ]; then
    mkdir -p $CONF
fi

# Note: -h is needed to prevent errors when trying to change dangling symlinks
chown -Rh signalk:signalk $CONF
