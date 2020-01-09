#! /bin/sh

if ! test -e /dev/gpio/digital_input_1; then
    svc -d .
    exit 0
fi

if ! test -e /dev/gpio/digital_input_1/edge; then
    flags=--poll=poll
fi

exec $(dirname $0)/dbus_digitalinputs.py $flags /dev/gpio/digital_input_*
