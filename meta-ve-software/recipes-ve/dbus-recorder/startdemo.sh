#!/bin/sh

dir=$(dirname $0)

# Stop services
svc -d /service/serial-starter
svc -d /service/vecan-mk2
svc -d /service/vecan
killall vedirect_dbus
killall gps_dbus

start() {
	${dir}/dbusrecorder.py -p --file="${dir}/$1" &
}

# Start services
start grid.dat
start pvinverter_fronius.dat
start solarcharger.dat
start vebus.dat
