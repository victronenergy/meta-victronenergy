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
SRCREV = "909276c88d17779347c2cbba55b0c350ef807ec9"
S = "${WORKDIR}/git"
QMAKE_PROFILES = "${S}/software/dbus-spy.pro"

