DESCRIPTION = "Make console available on tty port"
# Recipe is used on CCGX to make the console available on ttyO1, which doubles as
# VE.Direct port 1. Standard disabled, can be enabled from the menu

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# this is machine specific since machine-runtime is.
PACKAGE_ARCH = "${MACHINE_ARCH}"
RDEPENDS:${PN} = "machine-runtime-conf"

inherit ve_package
inherit daemontools

DAEMONTOOLS_RUN = "/sbin/getty -l /sbin/autologin -n 115200 \$(head /etc/venus/vedirect_and_console_port) linux"
DAEMONTOOLS_DOWN = "1"
