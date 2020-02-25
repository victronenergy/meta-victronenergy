#! /bin/sh

. /opt/victronenergy/serial-starter/run-service.sh

app=$(dirname $0)/dbus-modbus-client.py

start -x -s $tty
