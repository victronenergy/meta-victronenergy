#! /bin/sh

# list of directories to overlay with volatile storage
OVERLAYS="/etc /opt/victronenergy/service /service"

# base of overlays
OVL="/run/overlays"

for dir in ${OVERLAYS}; do
    upper=${OVL}${dir}
    work=${OVL}/work${dir}

    mkdir -p ${upper} ${work}

    mount -t overlay overlay \
          -o lowerdir=${dir},upperdir=${upper},workdir=${work} ${dir}
done
