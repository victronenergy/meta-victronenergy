#!/bin/sh
### BEGIN INIT INFO
# Provides:          container-versions
# Required-Start:
# Required-Stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: Stamp /var/run/versions from the running venus-version, container has no swupdate A/B partitions to scan
### END INIT INFO

# same "timestamp version" format swupdate-scripts' scan-versions.sh writes
sed -n -e '1h;3{G;s/\n/ /p}' /opt/victronenergy/version > /var/run/versions
