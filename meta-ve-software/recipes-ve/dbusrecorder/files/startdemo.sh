#!/bin/sh

# Stop services
svc -d /service/serial-starter
svc -d /service/vecan-mk2
svc -d /service/vecan
killall vedirect_dbus
killall gps_dbus

# Start services
/opt/color-control/dbusrecorder/dbusrecorder.py -p --file=/opt/color-control/dbusrecorder/gps.dat &
/opt/color-control/dbusrecorder/dbusrecorder.py -p --file=/opt/color-control/dbusrecorder/vebus.dat &
/opt/color-control/dbusrecorder/dbusrecorder.py -p --file=/opt/color-control/dbusrecorder/pvinverter.dat &
/opt/color-control/dbusrecorder/dbusrecorder.py -p --file=/opt/color-control/dbusrecorder/pvinverter_fronius.dat &
/opt/color-control/dbusrecorder/dbusrecorder.py -p --file=/opt/color-control/dbusrecorder/grid.dat &
