#!/bin/bash

HOME="/data/home/influxdb"

if [ ! -d $HOME ]; then
    mkdir -p $HOME
fi

chown -R influxdb:influxdb $HOME

