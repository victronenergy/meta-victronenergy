#!/bin/sh

case "$1" in
'start')
	# Log to indicate (re)start
	boot=`cat /tmp/last_boot_type`
	if [ "$boot" != "-3" ]; then
		for log in `ls /var/log/*/current 2> /dev/null`; do echo "*** CCGX booted ($boot) ***" | tai64n >> $log; done
	fi
	sync
	svscanboot &
	;;
'stop')
        svc -d /service/*
        svc -d /service/*/log
        killall svscan
        killall supervise
	;;
*)
	echo "Usage: $0 { start | stop }"
	;;
esac
