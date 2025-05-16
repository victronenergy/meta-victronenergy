SRC_URI = "git://github.com/victronenergy/venus-socketcan-test;branch=master;protocol=https"
SRCREV = "1e322a4fbbe19693bd1ec894424a404b4c36a487"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7c0ad2b90776f126b453c5f96c0da11a"
S = "${WORKDIR}/git"

RDEPENDS:${PN} += " \
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
