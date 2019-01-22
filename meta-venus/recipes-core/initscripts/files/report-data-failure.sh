#!/bin/sh
### BEGIN INIT INFO
# Provides:          log data partition failure
# Required-Start:
# Required-Stop:
# Default-Start:     5
# Default-Stop:
# Short-Description: down vrmlogger and report log partion failure
### END INIT INFO

fs=/run/data-partition-state

if [ "$1" != "start" ]; then
	exit 1
fi

if [ ! -f $fs ]; then
	exit 2
fi

error=0
if grep -q '^failed$' $fs; then
	error=3
fi

if grep -q '^failed-to-mount$' $fs; then
	error=4
fi

if [ $error -ne 0 ]; then
	# don't start vrmlogger, since it crashes in case of a ro /data
	touch /service/vrmlogger/down

	{
		# note: don't get it from /data, since it is broken!
		id=$(get-unique-id)

		# so lets log it ourselves
		while true; do
			ret=$(curl --user-agent "DataPartitionStatusPoster" --silent --fail --request POST --data "IMEI=$id" --data "c=100" --data "dps=$error" http://ccgxlogging.victronenergy.com/log/log.php)
			if [ "$ret" = "vrm: OK" ]; then
				exit 0
			fi
			sleep 60
		done
	} &
fi
