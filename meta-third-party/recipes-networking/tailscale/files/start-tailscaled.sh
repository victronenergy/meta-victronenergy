#!/bin/sh

# Ensure cgroup subsystems are mounted
# TODO: Spoke with Jeroen, we can add this to fstab (mounting cgroup) and then remove this code
if ! mount | grep -q "/sys/fs/cgroup type cgroup (rw,.*,cpu" || ! mount | grep -q "/sys/fs/cgroup type cgroup (rw,.*,cpuacct)"; then
    echo "Mounting cgroup v1 on /sys/fs/cgroup"
    mount -t cgroup -o cpu,cpuacct cgroup /sys/fs/cgroup
fi

# Create a cgroup for tailscaled
CGROUP_NAME="tailscaled"
CGROUP_PATH="/sys/fs/cgroup/$CGROUP_NAME"

if [ ! -d "$CGROUP_PATH" ]; then
    echo "Creating cgroup $CGROUP_PATH"
    cgcreate -g "cpu:/$CGROUP_NAME"
fi

# Set CPU quota to 10% of the total CPU time available on a multi-core system
TOTAL_CORES=$(nproc)
CPU_PERCENT=10
CPU_QUOTA=$((CPU_PERCENT * 1000 * TOTAL_CORES))

echo "Setting CPU quota to $CPU_QUOTA"
cgset -r cpu.cfs_quota_us=$CPU_QUOTA $CGROUP_NAME

# TODO: Remove after testing is finished
# Default value, used only for testing, can be removed later
echo "Setting CPU period to 100000"
cgset -r cpu.cfs_period_us=100000 $CGROUP_NAME

# Start the process in background to fetch the PID
exec /usr/sbin/tailscaled -no-logs-no-support -statedir /data/conf/tailscale &
TAILSCALED_PID=$!
echo "Started tailscaled with PID $TAILSCALED_PID"

# Wait 60 seconds before limiting CPU to speed up the connection
sleep 60

# Add the process to the cgroup
echo $TAILSCALED_PID > "$CGROUP_PATH/tasks"

# Wait for the process to finish
wait $TAILSCALED_PID
