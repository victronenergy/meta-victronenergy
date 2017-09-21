#!/bin/bash
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

function export_pin
{
    if [ -z ${1} ]
    then
        echo "ERR: export_pin needs an argument" >2
        exit 1
    fi

    echo $1 > /sys/class/gpio/export
}

function set_pin_dir
{
    if [[ -z ${1}  && ( ${1} != "out" && ${1} != "in" ) ]]
    then
        echo "ERR: set_pin_dir needs either 'in' or 'out' as an argument." >2
        exit 1
    fi
    if [ -z ${2} ]
    then
        echo "ERR: set_pin_dir needs an second argument" >2
        exit 1
    fi

    PIN=${2}
    FILE=$(echo /sys/class/gpio/gpio${PIN}/direction | tr -d '[[:space:]]')

    echo $1 > ${FILE}
}

function create_link
{
    if [[ -z ${1} || -z ${2} ]]
    then
        echo "ERR: create_link needs 2 arguments, an pin number and a name." >2
        exit 1
    fi

    PIN=${1}
    FILE=$(echo /sys/class/gpio/gpio${PIN}/value | tr -d '[[:space:]]')

    ln -s ${FILE} ${GPIO_DIR}/$2
}

function set_pin
{
    PIN_NUM=$1
    PIN_DIR=$2
    PIN_NAME=$3

    #echo "Setting gpio pin #${PIN_NUM}/${PIN_NAME} to ${PIN_DIR}"
    export_pin "${PIN_NUM}"
    set_pin_dir "${PIN_DIR}" "${PIN_NUM}"
    create_link "${PIN_NUM}" "${PIN_NAME}"
}

function check_compat
{
    cat /sys/firmware/devicetree/base/compatible | tr '\0' '\n' |
        grep -Eqx -e "$1"
}

mkdir -p ${GPIO_DIR}

sed 's/#.*//' ${GPIO_FILE} | while read num dir name compat
do
    # ignore incomplete lines
    if [ -z "$num" ] || [ -z "$dir" ] || [ -z "$name" ]
    then
        continue
    fi

    if [ -n "$compat" ]
    then
        check_compat "$compat" || continue
    fi

    set_pin "$num" "$dir" "$name"
done

