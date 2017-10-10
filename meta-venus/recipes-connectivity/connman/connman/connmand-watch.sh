#!/bin/sh

watch() {
        while [ true ]; do
                connmand "$1" --nodaemon > /dev/null 2>&1
                logger -t connmand_watch -p user.error  "the connmand process unexpectedly stopped ($?), restarting"
        done
}

watch "$@" &

