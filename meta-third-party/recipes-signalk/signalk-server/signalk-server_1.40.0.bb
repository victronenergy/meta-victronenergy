DESCRIPTION = "Node-RED"
HOMEPAGE = "http://nodered.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34f8c1142fd6208a8be89399cb521df9"

RDEPENDS_${PN} += "\
	bash \
"

SRC_URI = "\
	https://registry.npmjs.org/signalk-server/-/${PN}-${PV}.tgz;unpack=0 \
	file://start-signalk.sh \
	file://settings.json \
	file://logo.svg \
	file://venus.json \
	file://defaults.json \
"

SRC_URI[md5sum] = "8d31cbddd2a80075a45480d6a6a72fe9"
SRC_URI[sha256sum] = "dce512ccce4f5ddc35aa9799240f180977f1c48592ccd69f7634e296ac91ad54"

inherit npmve
inherit daemontools

DAEMON_PN = "${PN}"
DAEMONTOOLS_SCRIPT = "HOME=/home/root exec ${bindir}/signalk-server"
DAEMONTOOLS_DOWN = "1"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/signalk-server"

NPM_INSTALLDIR = "${D}${libdir}/node_modules/${PN}"

do_install_append() {

	mkdir ${D}${bindir}
	install -m 0755 ${WORKDIR}/start-signalk.sh ${D}${bindir}/signalk-server

	# this folder keeps the default settings. start-signalk.sh copies them
	# to the data partition on first boot.
	install -d ${NPM_INSTALLDIR}/defaults
	install -m 0755 ${WORKDIR}/settings.json ${NPM_INSTALLDIR}/defaults
	install -m 0755 ${WORKDIR}/defaults.json ${NPM_INSTALLDIR}/defaults
	install -m 0755 ${WORKDIR}/venus.json ${NPM_INSTALLDIR}/defaults
	install -m 0755 ${WORKDIR}/logo.svg ${NPM_INSTALLDIR}/defaults

	(cd ${NPM_INSTALLDIR}; npm --arch=${NPM_ARCH} --target_arch=${NPM_ARCH} install signalk-venus-plugin@1.24.14)

	# remove the files in put/test: they are compiled, though not cross-compiled thus
	# giving QA errors as well as being useless; and also they are not necessary
	rm -rf ${NPM_INSTALLDIR}/node_modules/put/test
}

