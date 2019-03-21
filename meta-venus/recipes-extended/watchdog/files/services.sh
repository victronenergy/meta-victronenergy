#! /bin/sh

WD_DIR=/run/watchdog
TIMEOUT=1

do_test() {
    dead_svc=$(find $WD_DIR -type f -mmin +$TIMEOUT | sed 's:.*/::')

    if [ -z "$dead_svc" ]; then
        exit 0
    fi

    logger "dead services: $dead_svc"

    exit -10
}

do_repair() {
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
