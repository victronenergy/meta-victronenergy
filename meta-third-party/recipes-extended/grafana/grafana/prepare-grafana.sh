#!/bin/bash

HOME="/data/conf/grafana"

if [ ! -d $HOME ]; then
    mkdir -p $HOME
fi

if [ ! -d $HOME/plugins ]; then
    mkdir -p $HOME/plugins
fi


# Note: -h is needed to prevent errors when trying to change dangling symlinks
chown -Rh grafana:grafana $HOME


