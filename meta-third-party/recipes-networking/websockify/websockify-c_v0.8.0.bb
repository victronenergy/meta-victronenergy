DESCRIPTION = "Websockify - C implementation"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://../docs/LICENSE.LGPL-3;md5=e6a600fd5e1d9cbde2d983680233ad02"
DEPENDS += "openssl"
RDEPENDS_${PN} += "openssl"

inherit ve_package
inherit daemontools

PR = "r1"
SRC_URI = " \
	git://github.com/kanaka/websockify.git;protocol=https;tag=${PV} \
	file://fix-buffer-overflow.patch \
"

S = "${WORKDIR}/git/other"
DEST_DIR = "${D}${bindir}"

# Note: used softlimits are arbitrary, no idea what they mean and what they should be

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 2000000 -s 1000000 -a 100000000 ${bindir}/websockify 0.0.0.0:81 127.0.0.1:5900"
DAEMONTOOLS_DOWN = "1"

do_install () {
	install -d ${D}${bindir}
	install -m 0755 ${S}/websockify ${DEST_DIR}/websockify
}
