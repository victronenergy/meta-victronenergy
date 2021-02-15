#!/bin/sh

# only called when starting
if [ "$1" = "start" ]; then
	test -x /data/rc.local && /data/rc.local
fi

# alternative, passing the argument
if test -x /data/initscript; then
	/data/initscript "$1"
fi

exit 0
