#!/bin/sh

# A runonce script runs (hopefully) after a sucessful update only once.

UPGRADE_D="/etc/upgrade.d"

if [ "$1" != "start" ]; then
	exit 0
fi

if [ -d $UPGRADE_D ]; then
	for script in `ls $UPGRADE_D`; do
		abs="$UPGRADE_D/$script"
		echo
		echo "running $abs"
		echo ----------------------------------------------------
		sh "$abs"
		echo ----------------------------------------------------
	done
fi

# Don't delete this script itself, but do remove any symlink from init to it
# The intention is to only run this once after an upgrade.
if [ -h $0 -o "$0" = "/etc/init.d/rc" ]; then
	echo
	echo removing symlink since we are are done
	rm /etc/rc5.d/S25postupgrade
fi
