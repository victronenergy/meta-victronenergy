#!/bin/sh

# workaround to make sure updated boards get rebooted.
PID=`pidof opkg`
if [ "$PID" = "" ]; then
	echo "opkg not running?"
	exit 0
fi

# wait for exit
while [ -d /proc/$PID ]; do sleep 10; done

# Normally update script should reboot after opkg update.
# But since it is only done when opkg exited succesfully and opkg can
# exit with a non zero exit while succesfully updating the system,
# always reboot after the distro version is changed.
sleep 60
echo "workaround for no reboot after update"
reboot
