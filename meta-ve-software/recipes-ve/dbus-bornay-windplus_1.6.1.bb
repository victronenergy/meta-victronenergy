DESCRIPTION = "Communication driver for Bornay Wind+ MPPT controller."
HOMEPAGE = "https://github.com/CarlosBornay/Bornay-venus-driver"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "\
    gitsm://github.com/victronenergy/${BPN}.git;protocol=https;branch=master \
"
SRCREV = "c3e415c97835ae3ddff0c4cdb88bf04cfb5e1df3"

S = "${WORKDIR}/git"

inherit gmakevelib
inherit daemontools
inherit python-compile

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/${PN}.py --serial TTY"
DAEMONTOOLS_DOWN = "1"
DAEMONTOOLS_SERVICE_SYMLINK = "0"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}.TTY"
DAEMONTOOLS_SERVICE_DIR = "${DAEMONTOOLS_TEMPLATE_DIR}"

RDEPENDS:${PN} = " \
    localsettings \
    python3-core \
    python3-dbus \
    python3-pymodbus \
"
