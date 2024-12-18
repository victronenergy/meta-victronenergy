DESCRIPTION = "Button handler"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit ve_package
inherit daemontools
inherit python-compile

RDEPENDS:${PN} = "\
    python3-core \
    python3-dbus \
    python3-evdev \
    python3-pygobject \
"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "\
    gitsm://github.com/victronenergy/${BPN}.git;branch=master;protocol=https \
"
SRCREV = "d554ffdac914532517267e3eb968607446ca1640"
S = "${WORKDIR}/git"

DAEMONTOOLS_RUN = "${bindir}/${PN} -D"

do_install () {
    oe_runmake DESTDIR=${D} install
}

do_configure[noexec] = "1"
do_compile[noexec] = "1"
