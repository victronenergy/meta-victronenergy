#! /bin/sh

inputs=$(find /dev/gpio/ -maxdepth 1 -name 'digital_input_*')

if [ -z "$inputs" ]; then
    svc -d .
    exit 0
fi

exec $(dirname $0)/dbus_digitalinputs.py $inputs
