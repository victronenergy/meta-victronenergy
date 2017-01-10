#!/bin/bash
### BEGIN INIT INFO
# Provides:          gpio_pins.sh
# Required-Start:
# Required-Stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: Exports gpio pins.
### END INIT INFO

if [ $(hw-revision) -gt 2 ]; then
	echo 1 > /sys/class/gpio/gpio155/active_low
	echo 1 > /sys/class/gpio/gpio153/active_low
fi
