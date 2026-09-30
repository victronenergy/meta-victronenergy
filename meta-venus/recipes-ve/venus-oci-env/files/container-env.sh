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
umask 022

# usage: save_port <variable name> <value> <output file>
save_port() {
    name=$1
    port=$2
    output=$3

    rm -f "$output"
    [ -n "$port" ] || return 0

    case "$port" in
        *[!0-9]*)
            echo "WARNING: ignoring invalid $name: $port" >&2
            return 0
            ;;
    esac

    if ! [ "$port" -ge 1 ] 2>/dev/null ||
       ! [ "$port" -le 65535 ] 2>/dev/null; then
        echo "WARNING: $name must be between 1 and 65535" >&2
        return 0
    fi

    printf '%s\n' "$port" > "$output.$$"
    mv -f "$output.$$" "$output"
}

save_port VENUS_HTTP_PORT "${VENUS_HTTP_PORT:-}" /run/venus/http-port
save_port VENUS_HTTPS_PORT "${VENUS_HTTPS_PORT:-}" /run/venus/https-port
