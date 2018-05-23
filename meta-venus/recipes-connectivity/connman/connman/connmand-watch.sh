#!/bin/sh

watch() {
        while [ true ]; do
                connmand "$@" --nodaemon > /dev/null 2>&1

                if [  "$(runlevel | awk '{ print $2 }')" = "6" ]; then
                        logger -t connmand_watch -p user.info  "no longer restarting the connmand process"
                        exit
                fi

                logger -t connmand_watch -p user.error  "the connmand process unexpectedly stopped ($?), restarting"
        done
}

watch "$@" &

