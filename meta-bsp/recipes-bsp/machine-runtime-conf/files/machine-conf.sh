#! /bin/sh

ID_FILE=/data/venus/unique-id

mkdir -p $(dirname ${ID_FILE})
id="$(get-unique-id)"
if [ "$id" != "" ]; then
    echo "$id" >${ID_FILE}
else
    echo "ERROR: failed to get an unique-id of the device"
fi

check_compat() {
    tr '\0' '\n' </sys/firmware/devicetree/base/compatible |
        grep -Eqx -e "$1"
}

gen_config() (
    conf=$1

    test -e ${conf}.in || return

    while IFS=% read val compat; do
        if [ -n "$compat" ]; then
            check_compat $compat || continue
        fi

        echo ${val}
    done <${conf}.in >/run/venus/$(basename ${conf})
)

CONFIGS="
    backlight_device
    blank_display_device
    can_names
    canbus_ports
    pwm_buzzer
"

mkdir -p /run/venus
for file in ${CONFIGS}; do
    gen_config /etc/venus/${file}
done
