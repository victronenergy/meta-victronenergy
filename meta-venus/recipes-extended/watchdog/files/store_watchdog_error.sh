#!/bin/sh

if [ "$#" -lt 1 ]; then
	echo "huh, expected an error code as argument" >&2
	exit 1
fi

# store it, so after the reboot failure can be reported
code="$1"
echo $code > /data/watchdog.reset

top -b -n1 > /data/log/watchdog_processlist.txt

exit $code
