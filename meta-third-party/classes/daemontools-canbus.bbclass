inherit daemontools-template

DAEMONTOOLS_LOG_DIR ?= "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}.DEV"
DAEMONTOOLS_TEMPLATE_CONF = "include can.inc"



