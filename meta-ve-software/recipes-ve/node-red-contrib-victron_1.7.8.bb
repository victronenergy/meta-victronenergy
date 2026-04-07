SUMMARY = "Victron Venus D-Bus plugin for node-red"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d7725b8b5e691085738d564afb312302"

SRC_URI = " \
    https://github.com/victronenergy/${BPN}/archive/refs/tags/v${PV}.tar.gz \
    file://remove-husky-for-venus.patch \
    file://npm-shrinkwrap.json;subdir=${S} \
"
INSANE_SKIP += "src-uri-bad"

# Careful! When updating the version, also npm-shrinkwrap.json must be updated
SRC_URI[sha256sum] = "cba4c913b1bc9019bf4e302d340fe3d8b441cdd8d1678a8cd3f4fff19bb0cf6e"

inherit npm-online-install

do_install:append() {
	rm -rf ${D}${nonarch_libdir}/node_modules/@victronenergy/${PN}/node_modules/put/test
}
