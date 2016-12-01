#! /bin/sh

rootdev=$(mountpoint -d /)

exec xdelta3 -d -c -DR -s /dev/block/${rootdev} >"$1"
