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
SRCREV = "6f260eadedfa0b9325c13e9001f2c234bea40692"
S = "${WORKDIR}/git"
QMAKE_PROFILES = "${S}/software/dbus-spy.pro"

