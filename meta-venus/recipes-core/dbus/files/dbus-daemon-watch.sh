#!/bin/sh

watch() {
	dbus-daemon "$1" --nofork
	logger -t dbus_watch -p user.error  "the dbus process unexpectedly stopped ($?), you have 5 minutes to fix it.."
	sleep 300
	logger -t dbus_watch -p user.info  "initiating reboot due to dbus failure."
	# In case the OOM killer killed dbus, simply rebooting may no longer
	# work. Hence trigger a watchdog reset.
	exec killall -STOP watchdog
}

watch "$@" &
