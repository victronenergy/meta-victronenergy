#!/bin/sh

http_port=80
if [ -r /run/venus/http-port ]; then
    configured_port=
    read -r configured_port < /run/venus/http-port || true

    case "$configured_port" in
        ''|*[!0-9]*)
            echo "WARNING: ignoring invalid OCI HTTP port: $configured_port" >&2
            ;;
        *)
            if [ "$configured_port" -ge 1 ] 2>/dev/null &&
               [ "$configured_port" -le 65535 ] 2>/dev/null; then
                http_port=$configured_port
            else
                echo "WARNING: ignoring out-of-range OCI HTTP port: $configured_port" >&2
            fi
            ;;
    esac
fi

while : ; do
    secure="$(dbus-send --system --print-reply --dest=com.victronenergy.settings /Settings/System/SecurityProfile com.victronenergy.BusItem.GetValue 2>/dev/null | grep variant | awk '{print $3;}')"
    if [ "$secure" != "" ]; then
        break
    fi
    sleep 1
done

mkdir -p /var/run/nginx/sites-enabled
rm -f /var/run/nginx/sites-enabled/*

enable_http_site() {
    site="$1"
    if [ "$http_port" = "80" ]; then
        ln -sf "/etc/nginx/sites-available/$site" /var/run/nginx/sites-enabled
    else
        sed -e "s/listen 80 default_server;/listen $http_port default_server;/" \
            -e "s/listen \[::\]:80 default_server;/listen [::]:$http_port default_server;/" \
            -e 's|http://\$host/|http://$host:$server_port/|g' \
            -e 's|\$scheme://\$host/|$scheme://$host:$server_port/|g' \
            -e 's|proxy_pass http://localhost/|proxy_pass http://127.0.0.1:$server_port/|' \
            "/etc/nginx/sites-available/$site" > "/var/run/nginx/sites-enabled/$site"
    fi
}

# The HTTPS site is unchanged and always enabled.
ln -sf /etc/nginx/sites-available/https.site /var/run/nginx/sites-enabled

# If node-red is installed, enable that as well (https).
if [ -f /etc/nginx/sites-available/node-red ]; then
    ln -sf /etc/nginx/sites-available/node-red /var/run/nginx/sites-enabled
fi

# 0: secure, only an explanation is available.
if [ "$secure" -eq 0 ]; then
    enable_http_site http-explanation.site
# 1: weak, 2: unsecure, 3: undetermined.
elif [ "$secure" -eq 1 ] || [ "$secure" -eq 2 ] || [ "$secure" -eq 3 ]; then
    enable_http_site http.site
else
    echo "error invalid secure level"
fi

# Common script, for example used for hiawatha as well.
for i in /etc/venus/www.d/*; do
    [ ! -f "$i" ] && continue
    "$i"
done

exec /usr/sbin/nginx
