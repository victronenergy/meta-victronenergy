#!/bin/sh

### BEGIN INIT INFO
# Provides:          container-sysfs
# Required-Start:    mountall
# Required-Stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: Enable sysfs hardware setup in a capable OCI container
### END INIT INFO

# Docker and Home Assistant Supervisor mount sysfs read-only for a
# non-privileged container.  Venus udev rules need to create I2C clients and
# export GPIOs for hardware such as the GX IO-Extender.  The OCI deployment
# grants CAP_SYS_ADMIN explicitly; use it in this container's mount namespace
# before udev starts.  The underlying sysfs objects still represent host
# kernel state, so this capability and remount are intentionally OCI-only.
if awk '$2 == "/sys" && $4 ~ /(^|,)ro(,|$)/ { found = 1 } END { exit !found }' /proc/mounts; then
    if ! mount -o remount,rw /sys; then
        echo "WARNING: unable to remount /sys read-write; I2C/GPIO hardware setup will be unavailable" >&2
    fi
fi
