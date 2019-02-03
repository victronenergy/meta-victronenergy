SUMMARY = "Victron Venus D-Bus plugin for node-red"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d7725b8b5e691085738d564afb312302"

#S = "${WORKDIR}/${PN}-${PV}"

NPM_ORG="victronenergy"

RDEPENDS_${PN} += "\
	node-red \
"

SRC_URI = "\
	https://registry.npmjs.org/@${NPM_ORG}/${PN}/-/${PN}-${PV}.tgz;unpack=0 \
	file://npm-shrinkwrap.json \
"

SRC_URI[md5sum] = "c7018e507067f7d7f75a37e9d91f43fe"
SRC_URI[sha256sum] = "a57eaad8ebdedce29afb6957eaeb3f8260ad02e06896b7285ae7251f777a27de"

inherit npmve

NPM_INSTALLDIR = "${D}${libdir}/node_modules/@${NPM_ORG}/${PN}"

do_install_append() {
        # Remove hardware specific files
	rm -r ${NPM_INSTALLDIR}/node_modules/put/test
}
