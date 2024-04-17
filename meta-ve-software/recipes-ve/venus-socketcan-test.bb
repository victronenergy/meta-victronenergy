SRC_URI = "git://github.com/victronenergy/venus-socketcan-test;branch=master;protocol=https"
SRCREV = "1004bfef93267002e919f2c8b8e8b16977ae3059"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7c0ad2b90776f126b453c5f96c0da11a"
S = "${WORKDIR}/git"

RDEPENDS:${PN} += " \
    canutils \
    can-utils \
    coreutils-stdbuf \
    python3-core \
    python3-json \
    python3-paramiko \
    python3-termcolor \
"

do_install () {
    install -d ${D}${bindir}
    install -m 755 ${S}/venus-socketcan-test.py ${D}${bindir}
}
