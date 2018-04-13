#!/bin/bash

. /opt/victronenergy/serial-starter/run-service.sh

app=$(dirname $0)/dbus-modem.py

start /dev/$tty
