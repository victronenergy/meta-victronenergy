#!/bin/bash
exec 2>&1
echo "*** Starting signalk ***"

CONF="/data/conf/signalk"
PLUGINCONF="${CONF}/plugin-config-data"
DEFAULTCONF="/usr/lib/node_modules/signalk-server/defaults"

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
    cp "$DEFAULTCONF/settings.json" "$CONF"
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
# export DEBUG=*
exec /usr/lib/node_modules/signalk-server/bin/signalk-server -c /data/conf/signalk
