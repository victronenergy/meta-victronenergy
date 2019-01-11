#!/bin/sh
### BEGIN INIT INFO
# Provides:          data check
# Required-Start:
# Required-Stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: check if data is mounted rw
### END INIT INFO

line=$(cat /proc/mounts | grep " /data ")
if [ -z $line ]; then
	error=1
fi

if echo $line | grep -q '[ ,]ro[ ,]'; then
	error=1
fi

if [ -n "$error" ]; then
        # make sure logs can be written so process don't block when trying to log.
        mount -t tmpfs shmfs -o size=12m /var/log/

        # Let the rest of the system know about	the issue
        touch /run/data-partition-failed
fi

