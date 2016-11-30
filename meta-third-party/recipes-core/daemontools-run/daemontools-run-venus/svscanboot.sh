#!/bin/sh

case "$1" in
'start')
	cp -Lr /service /run/
	mount --bind /run/service /service
	svscanboot &
	;;
'stop')
	;;
*)
	echo "Usage: $0 { start | stop }"
	;;
esac
