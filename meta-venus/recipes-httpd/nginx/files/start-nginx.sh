#!/bin/sh

while : ; do
	secure="$(dbus-send --system --print-reply --dest=com.victronenergy.settings /Settings/System/SecurityProfile com.victronenergy.BusItem.GetValue 2>/dev/null | grep variant | awk '{print $3;}')"
	if [ "$secure" != "" ]; then
		break;
	fi
	sleep 1
done

mkdir -p /var/run/nginx/sites-enabled
rm -f /var/run/nginx/sites-enabled/*

# the https version is always enabled
ln -sf /etc/nginx/sites-available/https.site /var/run/nginx/sites-enabled

# 0: secure, only an explanation is available
if [ "$secure" -eq 0 ]; then
    ln -sf /etc/nginx/sites-available/http-explanation.site /var/run/nginx/sites-enabled
# 1: weak & 2: unsecure (empty password file). 3: undetermined -> serve a page that a Secure Profile must be selected.
elif [ "$secure" -eq 1 ] || [ "$secure" -eq 2 ] || [ "$secure" -eq 3 ]; then
    ln -sf /etc/nginx/sites-available/http.site /var/run/nginx/sites-enabled
else
	echo "error invalid secure level"
fi

# common script, e.g. used for hiawatha as well.
for i in /etc/venus/www.d/*; do
	[ ! -f "$i" ] && continue
	"$i"
done

exec /usr/sbin/nginx
