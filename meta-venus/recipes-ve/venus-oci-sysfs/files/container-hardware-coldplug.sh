#!/bin/sh

### BEGIN INIT INFO
# Provides:          container-hardware-coldplug
# Required-Start:    udev
# Required-Stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: Replay devices missed by udev at OCI start
### END INIT INFO

case "$1" in
    start)
        found=
        for chip in /sys/class/gpio/gpiochip*; do
            [ -e "$chip/label" ] || continue
            # IO-Extender GPIO expander, created by io-extender.rules
            case "$(cat "$chip/label")" in
                *-0020)
                    udevadm trigger --action=add "$chip"
                    found=1
                    ;;
            esac
        done
        [ -z "$found" ] || udevadm settle

        # Serial devices passed into the container already exist when udev
        # starts, so no add event links them for serial-starter. udevadm test
        # fills the udev database from read-only sysfs but skips RUN keys.
        # /tmp is not a tmpfs here, so drop probe files from a previous run.
        rm -f /tmp/tty*.prog
        mkdir -p /run/serial-starter-tty
        for sys_tty in /sys/class/tty/*; do
            test -L "$sys_tty/device" || continue

            subsystem=$(basename "$(readlink "$sys_tty/device/subsystem" 2>/dev/null)")
            case $subsystem in
                platform|usb-serial) ;;
                *) continue ;;
            esac

            tty=${sys_tty##*/}
            test -e "/dev/$tty" || continue

            udevadm test --action=add "$sys_tty" >/dev/null 2>&1 || true
            ln -sf "/dev/$tty" "/run/serial-starter-tty/$tty"
        done
        ;;
esac

exit 0
