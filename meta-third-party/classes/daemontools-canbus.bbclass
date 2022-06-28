# note: a profile select the bitrate, hence serves are only started after
# the bitrate is set and hence down by default.
DAEMONTOOLS_SERVICE_SYMLINK = "0"
DAEMONTOOLS_LOG_DIR ?= "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}.DEV"
DAEMONTOOLS_SERVICE_DIR = "${DAEMONTOOLS_TEMPLATE_DIR}"

inherit daemontools

do_add_canbus_config() {
    mkdir -p "${D}/${DAEMONTOOLS_TEMPLATE_CONF_DIR}"
    echo "include can.inc" > "${D}/${DAEMONTOOLS_TEMPLATE_CONF_DIR}/${PN}.conf"
}

addtask add_canbus_config after do_install before do_package
do_add_canbus_config[fakeroot] = "1"
