#!/bin/sh

for i in /etc/venus/www.d/*; do
	[ ! -f "$i" ] && continue
	"$i"
done

exec /usr/sbin/nginx
