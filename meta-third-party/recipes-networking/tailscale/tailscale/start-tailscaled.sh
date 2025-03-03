#!/bin/sh
exec 2>&1

# Forward signals to the child process
trap 'kill -TERM $TAILSCALED_PID' TERM INT

# Ensure cgroup2 is mounted
if ! mount | grep -q "/sys/fs/cgroup type cgroup2 (rw,relatime)"; then
    echo "Mounting cgroup v2 on /sys/fs/cgroup"
    mount -t cgroup2 none /sys/fs/cgroup
fi

# enable cpu controller on the parent cgroup, if not already enabled
if ! grep -q "cpu" /sys/fs/cgroup/cgroup.subtree_control; then
    echo "Enabling +cpu in cgroup.subtree_control"
    echo "+cpu" > /sys/fs/cgroup/cgroup.subtree_control
fi

# Create a cgroup for tailscaled
CGROUP_NAME="tailscaled"
CGROUP_PATH="/sys/fs/cgroup/$CGROUP_NAME"

if [ ! -d "${CGROUP_PATH}" ]; then
    echo "Creating cgroup ${CGROUP_PATH}"
    mkdir "${CGROUP_PATH}"
fi

# Set CPU quota to 10% of the total CPU time available on a multi-core system
TOTAL_CORES=$(nproc)
CPU_PERCENT=10
CPU_QUOTA=$((CPU_PERCENT * 1000 * TOTAL_CORES))
CPU_PERIOD=100000
echo "Setting CPU quota to $CPU_QUOTA and period to $CPU_PERIOD"
echo "${CPU_QUOTA} ${CPU_PERIOD}" > "${CGROUP_PATH}/cpu.max"

# Start the process in background to fetch the PID
exec /usr/sbin/tailscaled -no-logs-no-support -statedir /data/conf/tailscale &
TAILSCALED_PID=$!
echo "Started tailscaled with PID $TAILSCALED_PID"

# Wait 60 seconds before limiting CPU to speed up the connection
sleep 60

# Add the process to the cgroup
echo $TAILSCALED_PID > "${CGROUP_PATH}/cgroup.procs"

# Wait for the process to finish
wait $TAILSCALED_PID

# Capture the exit status
EXIT_STATUS=$?

# Exit with the same status
exit $EXIT_STATUS
