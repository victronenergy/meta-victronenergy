DESCRIPTION = "Communication driver for Bornay Wind+ MPPT controller."
HOMEPAGE = "https://github.com/CarlosBornay/Bornay-venus-driver"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "\
    gitsm://github.com/victronenergy/${BPN}.git;protocol=http;branch=master;tag=v${PV} \
"

SRC_URI[md5sum] = "29b173fd5fa572ec0764d1fd7b527260"
SRC_URI[sha256sum] = "398a3db6d61899d25fd4a06c6ca12051b0ce171d705decd7ed5511517b4bb93d"

S = "${WORKDIR}/git"

inherit gmakevelib
inherit daemontools
inherit python-compile

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/${PN}.py --serial TTY"
DAEMONTOOLS_DOWN = "1"
DAEMONTOOLS_SERVICE_SYMLINK = "0"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}.TTY"

RDEPENDS_${PN} = " \
    localsettings \
    python \
    python-argparse \
    python-dbus \
    python-pymodbus \
"
