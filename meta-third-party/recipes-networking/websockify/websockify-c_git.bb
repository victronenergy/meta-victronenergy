DESCRIPTION = "Websockify - C implementation"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://../LICENSE.txt;md5=398df9650b825bc73ca671f91b5afc4b"
DEPENDS += "openssl"

inherit ve_package
inherit daemontools-template

SRC_URI = " \
    git://github.com/novnc/websockify-other.git;branch=master;protocol=https;rev=f0bdb0a621a4f3fb328d1410adfeaff76f088bfd \
"

S = "${WORKDIR}/git/other"
DEST_DIR = "${D}${bindir}"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/websockify 0.0.0.0:81 127.0.0.1:5900"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${B}/websockify ${DEST_DIR}/websockify
}
