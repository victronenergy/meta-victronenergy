#!/bin/sh

case "$1" in
'start')
	svscanboot &
	;;
'stop')
	;;
*)
	echo "Usage: $0 { start | stop }"
	;;
esac
