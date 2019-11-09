SRC_URI = "git://github.com/victronenergy/venus-socketcan-test"
SRCREV = "db7eee63f6a6e85b8add2ffb2d8355c012b02246"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7c0ad2b90776f126b453c5f96c0da11a"
S = "${WORKDIR}/git"

RDEPENDS_${PN} += " \
    canutils \
    can-utils \
    python3-argparse \
    python3-core \
    python3-json \
    python3-paramiko \
    python3-re \
    python3-subprocess \
    python3-termcolor \
"

do_install () {
    install -d ${D}${bindir}
    install -m 755 ${S}/venus-socketcan-test.py ${D}${bindir}
}
