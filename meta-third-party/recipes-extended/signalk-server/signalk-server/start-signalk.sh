#!/bin/bash
exec 2>&1
echo "*** Starting signalk ***"

CONF="/data/conf/signalk"
PLUGINCONF="${CONF}/plugin-config-data"
DEFAULTCONF="/usr/lib/node_modules/signalk-server/defaults"

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

# Remove plugins from /data that are, since v3.00, installed on root
# npm uninstall is used, rather than for example rm -rf, to also remove
# them from $CONF/package.json.
if [ -d "$CONF/node_modules/signalk-venus-plugin" ]; then
    echo "** signalk-venus-plugin is found on /data, running npm uninstall"
    npm --prefix $CONF uninstall signalk-venus-plugin
fi

if [ -d "$CONF/node_modules/signalk-n2kais-to-nmea0183" ]; then
    echo "** signalk-n2kais-to-nmea0183 is found on /data, running npm uninstall"
    npm --prefix $CONF uninstall signalk-n2kais-to-nmea0183
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
else
    settings_n2k=$(jq -r '.pipedProviders[] |
                    select(.id == "n2k-on-ve.can-socket") |
                    .pipeElements[].options.subOptions.interface' \
                    /data/conf/signalk/settings.json)

    if [ ! -e "/sys/class/net/$settings_n2k" ]; then
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

export PLUGINS_WITH_UPDATE_DISABLED="signalk-venus-plugin,signalk-n2kais-to-nmea0183,sk-to-nmea0183.json"
export SIGNALK_DISABLE_SERVER_UPDATES=true
export PRESERIALCOMMAND="/opt/victronenergy/serial-starter/stop-tty.sh"
export MFD_ADDRESS_SCRIPT="/usr/lib/node_modules/signalk-server/get-mfd-announce-address.sh"
export HOME="/data/conf/signalk"
# export DEBUG=*
exec /usr/lib/node_modules/signalk-server/bin/signalk-server -c /data/conf/signalk
