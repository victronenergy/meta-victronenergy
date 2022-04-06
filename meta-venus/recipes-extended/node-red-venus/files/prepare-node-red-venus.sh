#!/bin/sh

# Older version ran as root, so move / change the owner of the files
# if they are in the homedir of root.
NODE_RED_ROOT="/data/home/root/.node-red"
NODE_RED="/data/home/nodered/.node-red"

if [ ! -d /data/home/nodered ]; then
    mkdir -p /data/home/nodered
    chown nodered:nodered /data/home/nodered
fi

if [ ! -d $NODE_RED ] && [ -d $NODE_RED_ROOT ]; then
	chown -R nodered:nodered $NODE_RED_ROOT
	mv $NODE_RED_ROOT $NODE_RED
	sync
fi

