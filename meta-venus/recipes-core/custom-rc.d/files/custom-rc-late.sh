#!/bin/sh

# only called when starting
if [ "$1" = "start" ]; then
	if [ -x /data/rc.local ]; then
		touch /run/venus/custom-rc
		/data/rc.local
	fi
fi

# alternative, passing the argument
if test -x /data/initscript; then
	touch /run/venus/custom-rc
	/data/initscript "$1"
fi

exit 0
