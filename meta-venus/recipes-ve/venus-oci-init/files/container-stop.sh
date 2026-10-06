#!/bin/sh

### BEGIN INIT INFO
# Provides:          container-stop
# Required-Start:
# Required-Stop:
# Default-Start:     0 6
# Default-Stop:
# Short-Description: End the container once shutdown has finished
### END INIT INFO

# Handled once rc has finished, see container-init.
telinit u
