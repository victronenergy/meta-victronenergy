#!/bin/sh

# Real hardware never has this file; a container's early boot (before
# svscanboot strips the environment for everything under /service) writes
# any VENUS_*-prefixed variables here - see venus-container-env.
[ -f /run/venus/container-env ] && . /run/venus/container-env

while : ; do
	secure="$(dbus-send --system --print-reply --dest=com.victronenergy.settings /Settings/System/SecurityProfile com.victronenergy.BusItem.GetValue 2>/dev/null | grep variant | awk '{print $3;}')"
	if [ "$secure" != "" ]; then
		break;
	fi
	sleep 1
done

mkdir -p /var/run/nginx/sites-enabled
rm -f /var/run/nginx/sites-enabled/*

# Enable a plain-http site (listen 80), rewritten to VENUS_HTTP_PORT if that's
# set to something other than 80 - e.g. to free up port 80 on the host when
# running with --network host for CAN, where nginx would otherwise bind it
# unconditionally and collide with anything else already using it. Real
# hardware never sets this, so it always symlinks the file unmodified.
enable_http_site() {
	site="$1"
	if [ -n "$VENUS_HTTP_PORT" ] && [ "$VENUS_HTTP_PORT" != "80" ]; then
		sed -e "s/listen 80 default_server;/listen $VENUS_HTTP_PORT default_server;/" \
		    -e "s/listen \[::\]:80 default_server;/listen [::]:$VENUS_HTTP_PORT default_server;/" \
		    "/etc/nginx/sites-available/$site" > "/var/run/nginx/sites-enabled/$site"
	else
		ln -sf "/etc/nginx/sites-available/$site" /var/run/nginx/sites-enabled
	fi
}

# the https version is always enabled
ln -sf /etc/nginx/sites-available/https.site /var/run/nginx/sites-enabled

# if node-red is installed, enable that as well (https)
if [ -f /etc/nginx/sites-available/node-red ]; then
    ln -sf /etc/nginx/sites-available/node-red /var/run/nginx/sites-enabled
fi

# 0: secure, only an explanation is available
if [ "$secure" -eq 0 ]; then
    enable_http_site http-explanation.site
# 1: weak & 2: unsecure (empty password file). 3: undetermined -> serve a page that a Secure Profile must be selected.
elif [ "$secure" -eq 1 ] || [ "$secure" -eq 2 ] || [ "$secure" -eq 3 ]; then
    enable_http_site http.site
else
	echo "error invalid secure level"
fi

# common script, e.g. used for hiawatha as well.
for i in /etc/venus/www.d/*; do
	[ ! -f "$i" ] && continue
	"$i"
done

exec /usr/sbin/nginx
