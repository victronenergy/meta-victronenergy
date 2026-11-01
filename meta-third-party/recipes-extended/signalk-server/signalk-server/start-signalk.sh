#!/bin/bash
exec 2>&1
echo "*** Starting signalk ***"

CONF="/data/conf/signalk"
PLUGINCONF="${CONF}/plugin-config-data"
APPDIR="/usr/lib/node_modules/signalk-server"
DEFAULTCONF="$APPDIR/defaults"

n2k_interface=""

# - prefer vecan* over can* interfaces (no config or USB ones)
# - skip the bms-only ones.
# - prefer VE.Can as name (the Venus GX like devices have a non isolated can0,
#   called CAN 2 Port, while VE.Can is can1)
# - return the first matching one, e.g. vecan0 over vecan1 and can0 over can1.
#   (it is a shell script, it can't return values, it will be in n2k_interface)
find_n2k_canbus() {
    n2k_interface=""
    for f in /sys/class/net/*; do
        if [ ! -e "$f" ]; then
             break
        fi

        type=$(cat $f/type)
        if [ "$type" != "280" ]; then
            continue
        fi

        interface=$(basename $f)
        config=$(udevadm info -q property $f | sed -n  's/^VE_CAN_CONFIG=\(.*\)/\1/p')
        name=$(udevadm info -q property $f | sed -n  's/^VE_NAME=\(.*\)/\1/p')

        if [ "$config" != "bms-only" ]; then

            if [ "$n2k_interface" = "" ]; then
                n2k_interface="$interface"
            fi

            if [[ "$interface" =~ ^vecan.* ]]; then
                if ! [[ "$n2k_interface" =~ ^vecan.* ]]; then
                    n2k_interface="$interface"
                fi
            fi
            if [[ $name =~ ^VE.Can.* ]]; then
                n2k_interface="$interface"
                break
            fi
        fi
    done
}

# Remove plugins and webapps from /data when the rootfs has the same or a
# newer version, so that the rootfs version is used again. Only appstore
# installs: a version range in $CONF/package.json, not git or a path.
# npm uninstall also updates package.json, and keeps a copy that another
# plugin needs. With --offline, npm fails instead of fetching; then remove
# them by hand, else this repeats at every start.
outdated=$(node - "$CONF" "$APPDIR" <<'EOF'
const fs = require('fs');
const path = require('path');
const [conf, app] = process.argv.slice(2);
const semver = require(path.join(app, 'node_modules', 'semver'));
const read = (f) => {
    try {
        return JSON.parse(fs.readFileSync(f, 'utf8'));
    } catch (e) {
        return null;
    }
};
// The keywords by which signalk-server finds plugins and webapps, see
// https://github.com/SignalK/signalk-server/blob/master/src/interfaces/plugins.ts
// https://github.com/SignalK/signalk-server/blob/master/src/interfaces/webapps.ts
const keywords = ['signalk-node-server-plugin', 'signalk-webapp',
    'signalk-embeddable-webapp', 'signalk-wasm-plugin',
    'signalk-node-server-addon', 'signalk-plugin-configurator'];
const config = read(path.join(conf, 'package.json')) || {};
for (const [name, spec] of Object.entries(config.dependencies || {})) {
    const rootfs = read(path.join(app, 'node_modules', name, 'package.json'));
    if (!rootfs || !semver.validRange(spec) ||
            !(rootfs.keywords || []).some((k) => keywords.includes(k))) {
        continue;
    }
    const data = read(path.join(conf, 'node_modules', name, 'package.json'));
    if (!data || semver.lte(data.version, rootfs.version)) {
        console.log(name);
    }
}
EOF
)

if [ -n "$outdated" ]; then
    for name in $outdated; do
        echo "** $name is installed both on /data and on the rootfs, and the version on /data is the same or older:" \
            "running npm uninstall, and if that fails rm -rf and removing its entry from package.json"
    done
    if ! HOME="$CONF" npm --prefix "$CONF" uninstall --offline --ignore-scripts \
            --no-audit --no-fund --no-update-notifier $outdated; then
        echo "** npm uninstall failed, removing them by hand"
        for name in $outdated; do
            rm -rf "$CONF/node_modules/$name"
            jq --arg n "$name" 'del(.dependencies[$n])' "$CONF/package.json" > "$CONF/package.json.tmp" &&
                mv "$CONF/package.json.tmp" "$CONF/package.json" || rm -f "$CONF/package.json.tmp"
        done
    fi
fi

# Install the default settings, in case there are no settings yet
mkdir -p /data/conf/signalk/plugin-config-data

if [ ! -f "$CONF/settings.json" ]; then
    find_n2k_canbus
    if [ "$n2k_interface" != "" ]; then
        canbus_conf=$(sed "s/%CAN_INTERFACE%/$n2k_interface/g" "$DEFAULTCONF/canbus.json")
        jq '.pipedProviders += ['"$canbus_conf"']' < "$DEFAULTCONF/settings.json" > "$CONF/settings.json"
    else
        cp "$DEFAULTCONF/settings.json" "$CONF"
    fi
    touch /data/conf/signalk/.settings_canbus_check_done
else
    settings_n2k=$(jq -r '.pipedProviders[] |
                    select(.id == "n2k-on-ve.can-socket") |
                    .pipeElements[].options.subOptions.interface' \
                    /data/conf/signalk/settings.json)

    # note: older software doesn't set the CAN interface correctly, depending on the machine. This tries
    # to correct it, but only once. To not interfere with what the user sets.
    if [ ! -e "/sys/class/net/$settings_n2k" ] && [ ! -e "/data/conf/signalk/.settings_canbus_check_done" ]; then
        echo "The configured $settings_n2k CAN-bus doesn't exists!"
        find_n2k_canbus
        if [ "$n2k_interface" != "" ]; then
            echo "Changing it to $n2k_interface"
            jq --arg iface "$n2k_interface" '(.pipedProviders[] |
                    select(.id == "n2k-on-ve.can-socket") |
                    .pipeElements[].options.subOptions.interface) = $iface' \
                /data/conf/signalk/settings.json > /data/conf/signalk/settings.json.tmp
            mv /data/conf/signalk/settings.json.tmp /data/conf/signalk/settings.json
        fi
        touch /data/conf/signalk/.settings_canbus_check_done
    fi
fi

if [ ! -f "$CONF/defaults.json" ]; then
    cp "$DEFAULTCONF/defaults.json" "$CONF"
fi

if [ ! -f "$CONF/logo.svg" ]; then
    cp "$DEFAULTCONF/logo.svg" "$CONF"
fi

if [ ! -f "$PLUGINCONF/signalk-n2kais-to-nmea0183.json" ]; then
    cp "$DEFAULTCONF/signalk-n2kais-to-nmea0183.json" "$PLUGINCONF"
fi

if [ ! -f "$PLUGINCONF/sk-to-nmea0183.json" ]; then
    cp "$DEFAULTCONF/sk-to-nmea0183.json" "$PLUGINCONF"
fi

if [ ! -f "$PLUGINCONF/venus.json" ]; then
    cp "$DEFAULTCONF/venus.json" "$PLUGINCONF"
fi

sync

export SIGNALK_DISABLE_SERVER_UPDATES=true
export PRESERIALCOMMAND="/opt/victronenergy/serial-starter/stop-tty.sh"
export MFD_ADDRESS_SCRIPT="/usr/lib/node_modules/signalk-server/get-mfd-announce-address.sh"
export HOME="/data/conf/signalk"
# export DEBUG=*
exec /usr/lib/node_modules/signalk-server/bin/signalk-server -c /data/conf/signalk
