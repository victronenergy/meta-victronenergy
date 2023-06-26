LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SUMMARY = "dbus debug tool"
VELIB_DEFAULT_DIRS = "1"
inherit pkgconfig qmakeve

DEPENDS = "ncurses"

SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-spy.git;branch=master;protocol=https;tag=v${PV} \
"
S = "${WORKDIR}/git/software"

