#!/bin/sh

. $(dirname $0)/functions.sh

script="backup all data to usb stick"

echo "### ${script} starting"
notify "Backuping data to usb.." --no-beep

rsync --archive --info=progress2 --human-readable /data /media/sda/backup

sync

# Play notification to let the user know the script is done
notify "Backup done"

echo "### ${script} done"

exit 0
