#!/bin/sh
if test -x /data/rcS.local; then
    touch /run/venus/custom-rc
    /data/rcS.local
fi

exit 0
