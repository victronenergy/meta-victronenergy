#!/bin/sh

### BEGIN INIT INFO
# Provides:          container-sysfs
# Required-Start:    mountall
# Required-Stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: Enable sysfs hardware setup in a capable OCI container
### END INIT INFO

# Unless the container is privileged, the runtime mounts sysfs read-only.
# Venus udev rules write to it to create I2C clients and export GPIOs, for
# example for the GX IO-Extender. With CAP_SYS_ADMIN, remount it read-write
# in this container's mount namespace before udev starts. The sysfs objects
# still belong to the host kernel.
if awk '$2 == "/sys" && $4 ~ /(^|,)ro(,|$)/ { found = 1 } END { exit !found }' /proc/mounts; then
    if ! mount -o remount,rw /sys; then
        echo "WARNING: unable to remount /sys read-write; I2C/GPIO hardware setup will be unavailable" >&2
    fi
fi
