DESCRIPTION = "Make console available on tty port"
# Recipe is used on CCGX to make the console available on ttyO1, which doubles as
# VE.Direct port 1. Standard disabled, can be enabled from the menu

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS_${PN} = "machine-runtime-conf"

inherit ve_package
inherit daemontools

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "/sbin/getty -l /sbin/autologin -n 115200 \$(head /etc/venus/vedirect_and_console_port) linux"
DAEMONTOOLS_DOWN = "1"
