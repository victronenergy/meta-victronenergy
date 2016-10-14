#!/bin/sh

if [ $(awk '{print $1 < 60}' /proc/uptime) = "1" ]; then
	/opt/victronenergy/swupdate-scripts/check-updates.sh -force -offline -update
else
	echo "startup took more then 60 seconds!"
fi
