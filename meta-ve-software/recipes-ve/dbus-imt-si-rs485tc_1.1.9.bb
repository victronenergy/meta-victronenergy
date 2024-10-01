DESCRIPTION = "Communication driver for IMT Solar Irradiation sensors"
HOMEPAGE = "https://github.com/victronenergy/dbus-imt-si-rs485tc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit gmakevelib
inherit daemontools
inherit python-compile

SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-imt-si-rs485tc.git;branch=master;protocol=https;tag=${PV} \
    file://start.sh \
"

S = "${WORKDIR}/git"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/start.sh TTY"
DAEMONTOOLS_DOWN = "1"
DAEMONTOOLS_SERVICE_SYMLINK = "0"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}.TTY"
DAEMONTOOLS_SERVICE_DIR = "${DAEMONTOOLS_TEMPLATE_DIR}"

RDEPENDS:${PN} = " \
    bash \
    localsettings \
    python3-core \
    python3-dbus \
    python3-pymodbus \
    python3-pyserial \
"

do_install:append () {
    install -m 755 ${UNPACKDIR}/start.sh ${D}${bindir}
}
