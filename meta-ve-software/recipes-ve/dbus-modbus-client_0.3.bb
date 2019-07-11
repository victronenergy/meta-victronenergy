DESCRIPTION = "Modbus device handler"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit ve_package
inherit daemontools
inherit python-compile

RDEPENDS_${PN} = "\
    bash \
    python \
    python-dbus \
    python-pygobject \
    python-pymodbus \
"

SRC_URI = " \
    gitsm://github.com/victronenergy/${BPN}.git;protocol=ssh;user=git;tag=v${PV} \
"
S = "${WORKDIR}/git"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "${bindir}/${PN}.py"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}"

do_install () {
    oe_runmake DESTDIR=${D} install
}
