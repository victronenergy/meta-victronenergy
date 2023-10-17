#!/bin/sh

. $(dirname $0)/functions.sh

script="SignalK reset"

echo "### ${script} starting"
notify "SignalK resetting" --no-beep

for dir in /data/conf/signalk
do
  if [ -d ${dir} ]
  then
    echo "Removing ${dir}"
    rm -rf ${dir}
  fi
done

sync

# Play notification to let the user know the script is done
notify "SignalK reset done"

echo "### ${script} done"

exit 0
