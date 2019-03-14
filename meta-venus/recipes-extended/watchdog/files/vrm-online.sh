#! /bin/sh

get_value() {
    dbus-send --print-reply=literal --system --type=method_call \
              --dest=com.victronenergy.$1 $2 \
              com.victronenergy.BusItem.GetValue |
        awk '/int32/ { print $3 }'
}

do_test() {
    timeout=$(get_value settings /Settings/Watchdog/VrmTimeout)

    if [ "$timeout" = 0 ]; then
        # watchdog disabled
        exit 0
    fi

    enabled=$(get_value settings /Settings/Vrmlogger/Logmode)

    if [ "$enabled" != 1 ]; then
        # logging disabled, report success
        exit 0
    fi

    interval=$(get_value settings /Settings/Vrmlogger/LogInterval)
    last=$(get_value logger /Vrm/TimeLastContact)
    now=$(date +%s)
    totaltimeout=$((interval + timeout))

    if [ -z "$last" ]; then
        # no contact yet, use boot time
        last=$(awk '/^btime/ { print $2 }' /proc/stat)
        # make sure there is at least a 5 minutes time on a clean boot
        if [ $totaltimeout -lt 300 ]; then
            totaltimeout=300
        fi
    fi

    if [ $now -gt $((last + totaltimeout)) ]; then
        logger "VRM unreachable, rebooting"
        exit -10
    fi

    exit 0
}

do_repair() {
    /opt/victronenergy/dbus-modem/reset-modem.sh
    exit $1
}

case $1 in
    test)
        do_test
        ;;
    repair)
        do_repair $2
        ;;
esac
