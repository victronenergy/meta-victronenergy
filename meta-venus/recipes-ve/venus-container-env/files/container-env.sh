#!/bin/sh

### BEGIN INIT INFO
# Provides:          container-env
# Required-Start:    mountall
# Required-Stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: Capture VENUS_*-prefixed env vars for later container scripts
### END INIT INFO

mkdir -p /run/venus
env | grep '^VENUS_' > /run/venus/container-env
