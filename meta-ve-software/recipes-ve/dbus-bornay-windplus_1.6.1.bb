DESCRIPTION = "Communication driver for Bornay Wind+ MPPT controller."
HOMEPAGE = "https://github.com/CarlosBornay/Bornay-venus-driver"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    gitsm://github.com/victronenergy/${BPN}.git;protocol=http;branch=master;tag=v${PV} \
"

SRC_URI[sha256sum] = "398a3db6d61899d25fd4a06c6ca12051b0ce171d705decd7ed5511517b4bb93d"

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
