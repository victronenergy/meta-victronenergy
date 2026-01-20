#!/bin/bash
#
# The authorized_keys for the user vnctunnel needs to include the IP addresses
# of the EV chargers. We dynamically look them up.
#
# Weirdly, these two sources are in conflict about which source of public keys takes precedence.
#
# https://linux.die.net/man/5/sshd_config
# https://man.openbsd.org/sshd_config
#
# One says that the static file takes precedence, the other says the command
# takes precedence.
#
# Run time for system with one EV charger: 400 ms.

if [[ -z "$1"  ]]; then
  exit 0
fi

if [[ "$1" != "vnctunnel" ]]; then
  exit 0
fi

set -u

# Idea taken from vnc_ssh_sender.py, to not rely on a potentially corrupt or
# outdated public key file. The vnc_ssh_sender.py will take care of the private
# key always being correct.
if ! public_key_string=$(ssh-keygen -y -f /data/home/vnctunnel/.ssh/id_rsa); then
  exit 0
fi

extra_ips=()

while read -r service; do
  if [[ -z "$service" ]]; then
    continue
  fi
  addr=$(dbus-send --print-reply=literal --system --dest="$service" \
    /Mgmt/Connection com.victronenergy.BusItem.GetValue | awk 'NF>0{print $NF}')

  # Use actual socket API to validate that this is an IPv6 or IPv4 address.
  if python3 -c "import socket; print(socket.inet_pton(socket.AF_INET,  '$addr'))" &>/dev/null || \
     python3 -c "import socket; print(socket.inet_pton(socket.AF_INET6, '$addr'))" &>/dev/null
  then
    extra_ips+=("$addr")
  else
    continue
  fi
done <<< "$(dbus-send --print-reply --system --dest=org.freedesktop.DBus \
  /org/freedesktop/DBus org.freedesktop.DBus.ListNames | grep -Eo 'com\.victronenergy\.evcharger.\w*')"


get_wdu_settings_data_fake()
{
    cat << EOF
[{"host":"10.230.1.82","path":"settings-web/#","port":80},{"host":"10.230.1.73","path":"settings-web/#","port":8888}]
EOF
}

get_wdu_settings_data()
{
    if ! settingsurl_val=$(dbus-send --print-reply=literal --system \
        --dest=com.victronenergy.platform \
        '/EmpirBus/SettingsUrl' \
        com.victronenergy.BusItem.GetValue)
    then
        return 1
    fi

    echo "$settingsurl_val" | sed -E -e 's/^ +variant +//'
}

extra_ip_ports=()

if garmin_wdu_settings_dbus_output=$(get_wdu_settings_data); then
    while read -r line; do
        IFS=' ' read -r -a fields <<< "$line"

        if [[ "${#fields[@]}" -ne 2 ]]; then
            continue
        fi

        ip=${fields[0]}
        port=${fields[1]}

        if [[ "$ip" == *:* ]]; then
            ip="[$ip]"
        fi
        extra_ip_ports+=("$ip:$port")
    done < <(echo "$garmin_wdu_settings_dbus_output" | jq -r '.[] | "\(.host) \(.port)"' )
fi

permitopen=()

# only allow access to these services if VRM Portal is set to full
vrm_portal=$(dbus-send --print-reply=literal --system --dest=com.victronenergy.settings \
               /Settings/Network/VrmPortal com.victronenergy.BusItem.GetValue | awk '{print $3}')

if [ "$vrm_portal" = "2" ]; then
  permitopen+=("localhost:80")    # webserver, needed for unreleased gui-v2 wasm
  permitopen+=("localhost:1880")  # Node-RED
  permitopen+=("localhost:3000")  # Signal K
  permitopen+=("localhost:5900")  # VNC of gui-v1 for VRM

  for ip in "${extra_ips[@]}"; do
    permitopen+=("${ip}:80" "${ip}:443")
  done

  for ipport in "${extra_ip_ports[@]}"; do
    permitopen+=("${ipport}")
  done
fi

permitopen_flat=""

if [ ${#permitopen[@]} -ne 0 ]; then
  # convert to restrict,port-forwarding,permitopen="xxx",...
  IFS=
  permitopen=("${permitopen[@]/#/,permitopen=\"}")
  permitopen=("${permitopen[@]/%/\"}")
  permitopen_flat="port-forwarding${permitopen[*]}"
else
  permitopen_flat="no-port-forwarding"
fi

echo "restrict,${permitopen_flat} ${public_key_string}"
