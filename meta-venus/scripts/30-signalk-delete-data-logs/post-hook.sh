#!/bin/sh

. $(dirname $0)/functions.sh

script="SignalK delete data logs"

echo "### ${script} starting"
notify "SignalK deleting data logs" --no-beep

# Also remove hidden files
shopt -s dotglob
for dir in /data/log/signalk-server
do
  if [ -d ${dir} ]
  then
    echo "Removing all files from ${dir}"
    rm -r ${dir}/*
  fi
done

sync

# Play notification to let the user know the script is done
notify "SignalK deleting data logs done"

echo "### ${script} done"

exit 0
