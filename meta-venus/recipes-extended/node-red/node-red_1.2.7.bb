DESCRIPTION = "Node-RED"
HOMEPAGE = "http://nodered.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d6f37569f5013072e9490d2194d10ae6"

RDEPENDS_${PN} += "\
	bash \
"

SRC_URI = "\
	https://registry.npmjs.org/${PN}/-/${PN}-${PV}.tgz;unpack=0 \
	file://npm-shrinkwrap.json \
	file://start-node-red.sh \
"

SRC_URI[md5sum] = "9e4e183b252c89059035394cc172b1bf"
SRC_URI[sha256sum] = "0565a54a07a769b527b4d0c48e4d5c8dbdf030912ca324c43cc7cdc5394b2871"

inherit npmve
inherit daemontools

DAEMON_PN = "${PN}"
DAEMONTOOLS_SERVICE_DIR = "/etc/node-red/service"
DAEMONTOOLS_SCRIPT = "HOME=/home/root exec ${bindir}/node-red"
DAEMONTOOLS_DOWN = "1"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/node-red"

NPM_INSTALLDIR = "${D}${libdir}/node_modules/${PN}"

do_install_append() {
        # Remove hardware specific files
	rm ${NPM_INSTALLDIR}/bin/node-red-pi

	# Symlinks
	mkdir ${D}${bindir}
	#ln -s ${libdir}/node_modules/${PN}/red.js ${D}${bindir}/${PN}
	install -m 0755 ${WORKDIR}/start-node-red.sh ${D}${bindir}/${PN}
}

FILES_${PN} += " \
    ${bindir}/node-red \
"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
