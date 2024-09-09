#!/bin/sh

if [ -d /var/www/venus/gui-beta ]; then
	while : ; do
		gui_version="$(dbus-send --system --print-reply --dest=com.victronenergy.settings /Settings/Gui/RunningVersion com.victronenergy.BusItem.GetValue 2>/dev/null | grep variant | awk '{print $3;}')"
		if [ "$gui_version" != "" ]; then
			break;
		fi
		sleep 1
	done
else
	gui_version="1"
fi

mkdir -p /run/www
ln -sf /var/www/venus/gui-v${gui_version}.php /run/www/index.php

