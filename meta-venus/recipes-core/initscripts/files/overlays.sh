#!/bin/sh

# list of directories to overlay with volatile storage
OVERLAYS="/service:/opt/victronenergy/service"

# base of overlays
OVL="/run/overlays"

for dir in ${OVERLAYS}; do
    mount="${dir%%:*}"
    lower="${dir#*:}"
    work="${OVL}/work${mount}"
    upper="${OVL}${mount}"

    mkdir -p "${upper}" "${work}"

    mount -t overlay overlay \
          -o lowerdir=${lower},upperdir=${upper},workdir=${work} ${mount}
done
