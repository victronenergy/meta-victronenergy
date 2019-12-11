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
	echo 0 > /dev/gpio/mkx_rst/active_low
	echo 0 > /dev/gpio/vebus_standby/active_low
else
	echo 1 > /dev/gpio/mkx_rst/active_low
	echo 1 > /dev/gpio/vebus_standby/active_low
fi
