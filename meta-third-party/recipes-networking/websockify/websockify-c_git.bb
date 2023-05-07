DESCRIPTION = "Websockify - C implementation"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02"
DEPENDS += "openssl"

inherit ve_package
inherit daemontools-template

SRC_URI = " \
    git://github.com/novnc/websockify-other.git;branch=master;protocol=https;rev=05d6162e9e8ed060817c72de73ab4e7c62bb3da7 \
"

S = "${WORKDIR}/git"
B = "${WORKDIR}/git/c"
DEST_DIR = "${D}${bindir}"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/websockify 0.0.0.0:81 127.0.0.1:5900"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${B}/websockify ${DEST_DIR}/websockify
}
