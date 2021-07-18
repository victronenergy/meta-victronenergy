DESCRIPTION = "Venus HTML5 logger"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS_${PN} = "gwsocket"

inherit daemontools

DAEMONTOOLS_RUN = "setuidgid nobody softlimit -d 10000000 -s 1000000 -a 10000000 gwsocket --stdout"

