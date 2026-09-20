#!/bin/sh

### BEGIN INIT INFO
# Provides:          container-hardware-coldplug
# Required-Start:    udev
# Required-Stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: Replay GPIO devices created during OCI coldplug
### END INIT INFO

case "$1" in
    start)
        found=
        for chip in /sys/class/gpio/gpiochip*; do
            [ -e "$chip/label" ] || continue
            case "$(cat "$chip/label")" in
                *-0020)
                    udevadm trigger --action=add "$chip"
                    found=1
                    ;;
            esac
        done
        [ -z "$found" ] || udevadm settle
        ;;
esac

exit 0
