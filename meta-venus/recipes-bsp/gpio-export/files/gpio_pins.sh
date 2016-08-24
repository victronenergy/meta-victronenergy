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
    PIN_NUM=$(echo "${1}"  | awk '{print $1}')
    PIN_DIR=$(echo "${1}"  | awk '{print $2}')
    PIN_NAME=$(echo "${1}" | awk '{print $3}')

    #echo "Setting gpio pin #${PIN_NUM}/${PIN_NAME} to ${PIN_DIR}"
    export_pin "${PIN_NUM}"
    set_pin_dir "${PIN_DIR}" "${PIN_NUM}"
    create_link "${PIN_NUM}" "${PIN_NAME}"
}

mkdir -p ${GPIO_DIR}

while read line
do
    # ignore empty lines and lines where the first character is an '#'.
    if [[ -z ${line} || ! -z $(echo ${line} | grep -o "^[[:space:]]*#") ]]
    then
        continue
    fi

    #trim the spaces on the retrieved line.
    PIN_LINE=$(echo "${line}" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//' )
    set_pin "${PIN_LINE}"
done < ${GPIO_FILE}

