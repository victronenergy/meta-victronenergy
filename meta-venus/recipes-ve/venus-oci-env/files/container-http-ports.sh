#!/bin/sh

# The web sites listen on ports 80 and 443; serve them on VENUS_HTTP_PORT and
# VENUS_HTTPS_PORT instead when the OCI runtime sets them, see container-env.sh.

http_port="$(cat /run/venus/http-port 2>/dev/null)"
https_port="$(cat /run/venus/https-port 2>/dev/null)"

# usage: rewrite <site> <sed arguments>
rewrite() {
	site=$1
	shift
	link=/run/nginx/sites-enabled/$site
	[ -L "$link" ] || return 0
	sed "$@" "/etc/nginx/sites-available/$site" > "$link.tmp" && mv -f "$link.tmp" "$link"
}

if [ -n "$http_port" ] && [ "$http_port" != "80" ]; then
	for site in http.site http-explanation.site; do
		rewrite $site \
		    -e "s/listen 80 default_server;/listen $http_port default_server;/" \
		    -e "s/listen \[::\]:80 default_server;/listen [::]:$http_port default_server;/" \
		    -e 's|http://\$host/|http://$host:$server_port/|g' \
		    -e 's|\$scheme://\$host/|$scheme://$host:$server_port/|g' \
		    -e 's|proxy_pass http://localhost/|proxy_pass http://127.0.0.1:$server_port/|'
	done
fi

if [ -n "$https_port" ] && [ "$https_port" != "443" ]; then
	rewrite https.site \
	    -e "s/listen 443 ssl;/listen $https_port ssl;/" \
	    -e "s/listen \[::\]:443 ssl;/listen [::]:$https_port ssl;/" \
	    -e 's|https://\$host/|https://$host:$server_port/|g' \
	    -e 's|proxy_pass https://localhost/|proxy_pass https://127.0.0.1:$server_port/|'
fi
