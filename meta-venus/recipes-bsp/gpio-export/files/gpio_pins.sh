#!/bin/sh
### BEGIN INIT INFO
# Provides:          gpio_pins.sh
# Required-Start:
# Required-Stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: Exports gpio pins.
# Description:       Exports gpio pins from config file /etc/venus/gpio_list
### END INIT INFO

#reads ${GPIO_FILE} and performs export_pin and set_pin_dir on each line
#GPIO_FILE should be in the format:
# <number> <in/out>
# <number> <in/out>
#
#
#for example:
# 68 out
# 67 in

GPIO_DIR="/dev/gpio"
GPIO_FILE="/etc/venus/gpio_list"

export_pin() {
    echo $1 > /sys/class/gpio/export
}

set_pin_dir() {
    PIN=${2}
    FILE=/sys/class/gpio/gpio${PIN}/direction

    echo $1 > ${FILE}
}

create_link() {
    PIN=${1}
    FILE=/sys/class/gpio/gpio${PIN}/value

    ln -s ${FILE} ${GPIO_DIR}/$2
}

set_pin() {
    PIN_NUM=$1
    PIN_DIR=$2
    PIN_NAME=$3

    #echo "Setting gpio pin #${PIN_NUM}/${PIN_NAME} to ${PIN_DIR}"
    export_pin "${PIN_NUM}"
    set_pin_dir "${PIN_DIR}" "${PIN_NUM}"
    create_link "${PIN_NUM}" "${PIN_NAME}"
}

check_compat() {
    cat /sys/firmware/devicetree/base/compatible | tr '\0' '\n' |
        grep -Eqx -e "$1"
}

mkdir -p ${GPIO_DIR}

sed 's/#.*//' ${GPIO_FILE} | while read num dir name compat; do
    # ignore incomplete lines
    if [ -z "$num" ] || [ -z "$dir" ] || [ -z "$name" ]; then
        continue
    fi

    if [ -n "$compat" ]; then
        check_compat "$compat" || continue
    fi

    set_pin "$num" "$dir" "$name"
done

