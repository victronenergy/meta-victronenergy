#! /bin/sh

if ! test -e /dev/gpio/digital_input_1; then
    svc -d .
    exit 0
fi

exec $(dirname $0)/dbus_digitalinputs.py /dev/gpio/digital_input_*
