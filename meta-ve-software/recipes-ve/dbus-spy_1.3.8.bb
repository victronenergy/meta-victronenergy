LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SUMMARY = "dbus debug tool"
VELIB_DEFAULT_DIRS = "1"

DEPENDS = "ncurses"

inherit pkgconfig qmakeve

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-spy.git;branch=master;protocol=https \
"
SRCREV = "0d86265aedae5d158fa2f0543c8342890cbb08b6"
S = "${WORKDIR}/git"
QMAKE_PROFILES = "${S}/software/dbus-spy.pro"

