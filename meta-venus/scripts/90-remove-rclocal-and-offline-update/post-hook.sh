#!/bin/sh

script="remove local rc-scripts and install firmware"

if [ -f /opt/victronenergy/prodtest/buzzer.sh ]; then
    BEEP="/opt/victronenergy/prodtest/buzzer.sh"
elif [ -f /opt/victronenergy/prodtest/buzzer.py ]; then
    BEEP="/opt/victronenergy/prodtest/buzzer.py"
else
    BEEP="echo beep"
fi

beep() {
    ${BEEP} 1
    sleep 0.2
    ${BEEP} 0
}

error() {
	echo $1
    ${BEEP} 1
    sleep 3
    ${BEEP} 0
}

echo "### ${script} starting"
beep

[ -e /data/rc.local ] && rm -f /data/rc.local
[ -e /data/rcS.local ] && rm -f /data/rcS.local
sync

# prevent that check-updates reboots thee device, sine it will cause an
# endless firmware update loop.
tmp="$(mktemp -d)"
echo "#!/bin/sh" > "$tmp/reboot"
chmod +x "$tmp/reboot"
export PATH="$tmp:$PATH"

if ! /opt/victronenergy/swupdate-scripts/check-updates.sh -offline -update -force; then
	error "Swupdate failed!"
	exit 1
fi

beep && sleep 1 && beep

echo "### ${script} done, remove the media and reset"

sleep infinity
