#! /bin/sh

ID_FILE=/data/venus/unique-id

if [ ! -f ${ID_FILE} ]; then
    mkdir -p $(dirname ${ID_FILE})
    get-unique-id >${ID_FILE}
fi
