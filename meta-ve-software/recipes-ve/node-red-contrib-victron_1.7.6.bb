SUMMARY = "Victron Venus D-Bus plugin for node-red"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d7725b8b5e691085738d564afb312302"

SRC_URI = " \
    https://github.com/victronenergy/${BPN}/archive/refs/tags/v${PV}.tar.gz \
    file://remove-husky-for-venus.patch \
    file://npm-shrinkwrap.json;subdir=${S} \
"
INSANE_SKIP += "src-uri-bad"
PR = "1"

# Careful! When updating the version, also npm-shrinkwrap.json must be updated
SRC_URI[sha256sum] = "d7c3aa68a4e89aed98f4e0490b9a7901a44122b1de924fd9ce49554e05bd0e9e"

inherit npm-online-install

do_install:append() {
	rm -rf ${D}${nonarch_libdir}/node_modules/@victronenergy/${PN}/node_modules/put/test
}
