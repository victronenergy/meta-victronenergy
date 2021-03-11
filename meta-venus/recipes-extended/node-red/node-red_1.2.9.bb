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
	file://settings.js \
	file://start-node-red.sh \
	file://user-authentication.js \
"

SRC_URI[md5sum] = "394a7aba427ee8ada729bb0830a59e77"
SRC_URI[sha256sum] = "ce7984f40a9bbb8342faeaefc8d863de4807ddf84953078d459e8b7209a8ca4d"

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

	# this folder keeps the default settings. start-node-red.sh copies them
	# to the data partition on first boot.
	install -d ${NPM_INSTALLDIR}/defaults
	install -m 0755 ${WORKDIR}/settings.js ${NPM_INSTALLDIR}/defaults
	install -m 0755 ${WORKDIR}/user-authentication.js ${NPM_INSTALLDIR}/defaults

	# Symlinks
	mkdir ${D}${bindir}
	#ln -s ${libdir}/node_modules/${PN}/red.js ${D}${bindir}/${PN}
	install -m 0755 ${WORKDIR}/start-node-red.sh ${D}${bindir}/${PN}
}

FILES_${PN} += " \
    ${bindir}/node-red \
"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
