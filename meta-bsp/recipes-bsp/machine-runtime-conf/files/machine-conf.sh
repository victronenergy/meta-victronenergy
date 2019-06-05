#! /bin/sh

ID_FILE=/data/venus/unique-id

mkdir -p $(dirname ${ID_FILE})
get-unique-id >${ID_FILE}

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
    done <${conf}.in >${conf}
)

CONFIGS="
    backlight_device
    blank_display_device
    canbus_ports
"

for file in ${CONFIGS}; do
    gen_config /etc/venus/${file}
done
