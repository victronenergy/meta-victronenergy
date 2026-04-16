DESCRIPTION = "Button handler"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit ve_package
inherit python-compile

RDEPENDS:${PN} = "\
    python3-core \
    python3-dbus \
    python3-logging \
    python3-threading \
"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/${BPN}.git;branch=master;protocol=https \
"
SRCREV = "669e1846c85d64661c6b5f59e8bc5a394570c98e"
S = "${WORKDIR}/git"

do_install () {
    oe_runmake DESTDIR=${D} install
}

do_configure[noexec] = "1"
do_compile[noexec] = "1"
