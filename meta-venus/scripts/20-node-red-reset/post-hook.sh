#!/bin/sh

. $(dirname $0)/functions.sh

script="Node-RED reset"

echo "### ${script} starting"
notify "Node-RED resetting" --no-beep

for dir in /data/home/nodered/{.node-red,.npm}
do
  if [ -d ${dir} ]
  then
    echo "Removing ${dir}"
    rm -rf ${dir}
  fi
done

sync

# Play notification to let the user know the script is done
notify "Node-RED reset done"

echo "### ${script} done"

exit 0
