LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SUMMARY = "dbus debug tool"
VELIB_DEFAULT_DIRS = "1"

DEPENDS = "ncurses"
QT6_DEPENDS = "qtbase"

inherit pkgconfig qmakeve

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-spy.git;branch=master;protocol=https \
"
SRCREV = "8015d34d3af403d0e0880d53d8b13f5b0da7cd50"
S = "${WORKDIR}/git/software"

