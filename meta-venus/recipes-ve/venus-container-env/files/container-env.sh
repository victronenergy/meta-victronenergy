#!/bin/sh

### BEGIN INIT INFO
# Provides:          container-env
# Required-Start:    mountall
# Required-Stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: Preserve validated OCI runtime configuration
### END INIT INFO

mkdir -p /run/venus

output=/run/venus/http-port
rm -f "$output"

port=${VENUS_HTTP_PORT:-}
[ -n "$port" ] || exit 0

case "$port" in
    *[!0-9]*)
        echo "WARNING: ignoring invalid VENUS_HTTP_PORT: $port" >&2
        exit 0
        ;;
esac

if ! [ "$port" -ge 1 ] 2>/dev/null ||
   ! [ "$port" -le 65535 ] 2>/dev/null; then
    echo "WARNING: VENUS_HTTP_PORT must be between 1 and 65535" >&2
    exit 0
fi

tmp="$output.$$"
trap 'rm -f "$tmp"' EXIT HUP INT TERM
umask 022
printf '%s\n' "$port" > "$tmp"
mv -f "$tmp" "$output"
trap - EXIT HUP INT TERM
