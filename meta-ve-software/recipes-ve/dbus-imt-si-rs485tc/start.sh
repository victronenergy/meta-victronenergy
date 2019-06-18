#!/bin/bash

. /opt/victronenergy/serial-starter/run-service.sh

app=$(dirname $0)/dbus-imt-si-rs485tc.py

start $tty
