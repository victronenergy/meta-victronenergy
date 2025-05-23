SUMMARY = "Victron Venus D-Bus plugin for node-red"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d7725b8b5e691085738d564afb312302"

SRC_URI = " \
    https://github.com/victronenergy/${BPN}/archive/refs/tags/v${PV}.tar.gz \
    file://npm-shrinkwrap.json;subdir=${S} \
"
INSANE_SKIP += "src-uri-bad"

# Careful! When updating the version, also npm-shrinkwrap.json must be updated
SRC_URI[sha256sum] = "25ec9fcc3fef96d81e36f0131b491a409128b031152a3e74332ef89aa3dcd74a"

inherit npm-online-install

do_install:append() {
	rm -rf ${D}${nonarch_libdir}/node_modules/@victronenergy/${PN}/node_modules/put/test
}
