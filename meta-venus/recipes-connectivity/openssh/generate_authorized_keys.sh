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

extra_permitopen=""
for ip in "${extra_ips[@]}"; do
  extra_permitopen="${extra_permitopen},permitopen=\"${ip}:80\",permitopen=\"${ip}:443\""
done

full_permitopen='restrict,port-forwarding,permitopen="localhost:5900",permitopen="localhost:1880",permitopen="localhost:3000",permitopen="localhost:80"'

if [[ -n "$extra_permitopen" ]]; then
  full_permitopen="${full_permitopen}${extra_permitopen}"
fi

echo "${full_permitopen} ${public_key_string}"
