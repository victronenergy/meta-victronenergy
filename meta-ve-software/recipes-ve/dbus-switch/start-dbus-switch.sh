#! /bin/sh
#
# Start script dbus-switch GX IO extender
#       $tty comes from the serial starter but is actually the serial number of te io extender

. /opt/victronenergy/serial-starter/run-service.sh
app=/opt/victronenergy/dbus-switch/dbus-switch.py

start --serial $tty