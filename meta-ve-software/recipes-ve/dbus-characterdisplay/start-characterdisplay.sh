#! /bin/sh

if [ ! -e /dev/lcd ]; then
    svc -d .
    exit 0
fi

exec $(dirname $0)/dbus_characterdisplay.py
