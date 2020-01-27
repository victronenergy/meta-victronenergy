DESCRIPTION = "Button handler"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit ve_package
inherit daemontools
inherit python-compile

RDEPENDS_${PN} = "\
    python \
    python-argparse \
    python-dbus \
    python-evdev \
    python-pygobject \
"

SRC_URI = "\
    gitsm://github.com/victronenergy/${PN}.git;protocol=https;tag=v${PV} \
"
S = "${WORKDIR}/git"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "${bindir}/${PN} -D"

do_install () {
    oe_runmake DESTDIR=${D} install
}

do_configure[noexec] = "1"
do_compile[noexec] = "1"
