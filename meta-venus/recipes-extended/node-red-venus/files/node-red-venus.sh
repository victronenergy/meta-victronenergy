#!/bin/bash
exec 2>&1

NODE_RED="/data/home/nodered/.node-red"
DATA_MODULES="$NODE_RED/node_modules"
VICTRON_MODULES="$DATA_MODULES/@victronenergy"

get_setting() {
    dbus-send --print-reply=literal --system --type=method_call \
              --dest=com.victronenergy.settings "$1" \
              com.victronenergy.BusItem.GetValue |
        awk '{ print $NF }'
}

# There used to be a random generated secret stored in settings.js, the intention
# is that that is known by the user. Node-red will do that itself and store it in
# ~/.node-red/.config.runtime.json. To be backwards compatible, move it from
# settings.js to settings-user.js. /usr/lib/node_modules/node-red/venus-settings.js
# will include that file and ~/.node-red/settings.js is no longer used so rename it
# to settings.js.old.

move_secret() {
    if [ -e "$NODE_RED/settings.js" ] && [ ! -e "$NODE_RED/settings-user.js" ]; then

        secret=$(sed -ne 's/[[:space:]]*credentialSecret: "\([0-9a-f]\+\)".*/\1/p' "$NODE_RED/settings.js")

        if [ "$secret" != "" ]; then
            cat <<EOF > "$NODE_RED/settings-user.js"
module.exports = {
    credentialSecret: "$secret",
}
EOF
        fi

        mv "$NODE_RED/settings.js" "$NODE_RED/settings.js.old"
        sync
    fi
}

rename_flows() {
    flows="$NODE_RED/flows.json"

    # never overwrite an existing flow file. Note: it will only exists after a user
    # deployed it.
    if [ -e "$flows" ]; then
        return
    fi

    host="$(hostname)"
    flows_old="$NODE_RED/flows_${host}.json"
    cred="$NODE_RED/flows_cred.json"
    cred_old="$NODE_RED/flows_${host}_cred.json"

    # rename flow file which contains the hostname, if it exists..
    if [ -f "$flows_old" ]; then

        # and its credentials
        if [ -f "$cred_old" ]; then
            cp "$cred_old" "$cred"
        fi

        cp "$flows_old" "$flows"
        sync

        # move the original files, so they don't replace a non-existing flows.json again.
        if [ -f "$cred_old" ]; then
            mv "$cred_old" "$cred_old.renamed"
        fi
        mv "$flows_old" "$flows_old.renamed"
        sync
    fi
}

echo "*** Waiting for localsettings..."

while true; do
    mode=$(get_setting /Settings/Services/NodeRed) && break
    sleep 1
done

case $mode in
    0)
        echo "*** Node-RED disabled in settings, starting anyway"
        safe=
        ;;
    1)
        echo "*** Starting in normal mode"
        safe=
        ;;
    2)
        echo "*** Starting in safe mode"
        safe=--safe
        ;;
    *)
        echo "*** Invalid setting $mode"
        exit 1
        ;;
esac

if [ ! -d $NODE_RED ]; then
    mkdir -p "$NODE_RED"
    mkdir -p "$DATA_MODULES"
fi

if [ -d $VICTRON_MODULES ]; then
    rm -rf $VICTRON_MODULES
fi

if [ ! -f $VICTRON_MODULES ]; then
    touch $VICTRON_MODULES
fi

move_secret
rename_flows

export TZ=$(get_setting /Settings/System/TimeZone)

exec /usr/bin/node-red $safe --userDir "${NODE_RED}" --settings /usr/lib/node_modules/node-red/venus-settings.js "$@"
