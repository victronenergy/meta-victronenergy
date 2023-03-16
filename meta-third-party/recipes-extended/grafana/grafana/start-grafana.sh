#!/bin/bash
exec 2>&1
echo "*** Starting grafana ***"

PATH="/usr/share/grafana/bin:$PATH" 

HOME="/data/conf/grafana"
GRAFANA_HOME="/usr/share/grafana"

# This might need to change, as /etc is readonly
CONF_DIR=/etc/grafana
CONF_FILE=$CONF_DIR/grafana.ini
DATA_DIR=$HOME
PLUGINS_DIR=$HOME/plugins
PROVISIONING_CFG_DIR=$GRAFANA_HOME/provisioning

# These values should probably live in dBus and be fetched
# so that they can be managed via the GUI.
export VGS_INFLUXDB_URL="http://127.0.0.1:8086"
export VGS_INFLUXDB_USERNAME=""
export VGS_INFLUXDB_PASSWORD=""

# Define the default dashboard to be presented
export GF_DASHBOARDS_DEFAULT_HOME_DASHBOARD_PATH=$GRAFANA_HOME/dashboards/venus-dashboard.json

exec grafana-server                                         \
  --homepath="$GRAFANA_HOME"                                \
  --config="$CONF_FILE"                                     \
  "$@"                                                      \
  cfg:default.log.mode="console"                            \
  cfg:default.log.level="warn"                              \
  cfg:default.paths.data="$DATA_DIR"                        \
  cfg:default.paths.plugins="$PLUGINS_DIR"                  \
  cfg:default.paths.provisioning="$PROVISIONING_CFG_DIR"
