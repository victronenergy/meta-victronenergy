#!/bin/sh

. $(dirname $0)/functions.sh

script="Reset all"

echo "### ${script} starting"
notify "Resetting all" --no-beep

# remove everything from /data/ besides venus/
echo "removing files from /data"
find /data -mindepth 1 -maxdepth 1 ! -path /data/venus ! -exec rm -rf "{}" \;
sync

# remove this script, it should only run once (and should be done now).
echo "done"
rm /data/rcS.local

sync

# Play notification to let the user know the script is done
notify "Resetting all done"

echo "### ${script} done - rebooting"

# since also directories etc are removed, which were created earlier
# in the boot process, lets reboot so they will be recreated.
reboot
