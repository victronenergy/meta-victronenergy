#! /bin/sh

if [ ! -e /dev/modem ]; then
    # no modem, nothing to do
    exit 0
fi

# start background process waiting for device to go away
inotifywait -qq -e delete_self -t 150 /dev/modem &
devwaitpid=$!

# bring down modem manager and pppd, stopping modem watchdog updates
svc -d /service/dbus-modem.*
svc -d /service/ppp

# reset built-in modem
if [ -e /dev/gpio/modem_rst ]; then
    echo 1 >/dev/gpio/modem_rst/value
fi

# wait for device to go away
wait $devwaitpid
