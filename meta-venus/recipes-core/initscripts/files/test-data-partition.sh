#!/bin/sh
### BEGIN INIT INFO
# Provides:          data partition check
# Required-Start:
# Required-Stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: monitor if data doesn't becomes ro
### END INIT INFO

# Monitor that the data partition doesn't get remounted as ro, as in filesystem
# trouble, mainly intented for ubi. Note: is-ro-partition depends on a small
# linux patch to notify this script that auto ro remount has occured. If that
# happens somewhere early in init the machine won't boot since many processes
# keep spinning and most of the cpu time is spend in "sys". The watchdog will
# kick in before something is shown and it creates a continues reboot loop. For
# completeness, if it happens later on, the machine keeps running, but fails in
# odd manners since files / settings can no longer be stored. So instead,
# directly reboot when this happens and store that it occured. Since writing to
# data is no longer possible, u-boot env is used instead.

# There seems to be two different cases. One is recoverable with a reboot, the
# other one, ECC failures, is persistant. So the following is done, if data
# becomes ro the first time, it is stored in u-boot env and the machine is
# rebooted. If after a reboot the data parition doesn't become ro for quite
# some time, u-boot env is updated that the device had an incident, but
# recovered from it. If it becomes ro again, the device is marked as broken
# and whatever is needed to report that event to the user and vrm is done.

if [ "$1" != "start" ]; then
	exit 1
fi

sf=/run/data-partition-state
uvar=data-failed-count

# first, lets see if data is mounted at all
mounted=1
line=$(cat /proc/mounts | grep " /data ")
if [ -z "$line" ]; then
	echo "data partition is not mounted!"
	echo failed-to-mount > $sf
	mounted=0
fi

mkdir /run/lock
count=$(fw_printenv "$uvar" 2>/dev/null)

if [ "${count}" = "$uvar=2" ]; then
	echo "data partition failed!"
	echo failed > $sf
fi

# If the data partition became read-only more then once, it is really broken
if [ $mounted -eq 0 ] || [ "${count}" = "$uvar=2" ]; then

	# make sure logs can be written so process don't block when trying to log.
	# The device cannot boot without it, since the watchdog will reset it.
	mount -t tmpfs shmfs -o size=12m /data/log/

	# Just leave the data partion read-only, broken device...
	exit
fi

# If data became ro once, check if this happens again
if [ "$count" = "$uvar=1" ]; then
	echo "data partition failed once!"
	echo failed-once >  $sf

	{
		sleep 86400
		echo "data partition failed once, but seems recovered"
		fw_setenv "$uvar" 0
		echo recovered > $sf
	} &

else
	if [ "$count" = "$uvar=0" ]; then
		echo "data partition is recovered"
		echo recovered > $sf
	else
		echo "data partition is fine"
		echo fine > $sf
	fi
fi

{
	if is-ro-partition /data; then
		# if data became ro again, just mark it as failed and reboot to report it
		if [ "$count" = "$uvar=1" ]; then
			fw_setenv "$uvar" 2
		else
			# first time, lets see if rebooting helps
			fw_setenv "$uvar" 1
		fi

		reboot
	fi
} &
