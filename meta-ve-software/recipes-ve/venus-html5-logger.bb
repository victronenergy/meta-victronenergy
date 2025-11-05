DESCRIPTION = "Venus HTML5 logger"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS:${PN} = "gwsocket"

inherit daemontools

DAEMONTOOLS_RUN = "setuidgid nobody ${@softlimit(d, data=10000000, stack=1000000, all=100000000)} gwsocket --stdout"
DAEMONTOOLS_DOWN = "1"
