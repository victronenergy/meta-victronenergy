SUMMARY = "Victron Venus D-Bus plugin for node-red"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d7725b8b5e691085738d564afb312302"

SRC_URI = " \
    https://github.com/victronenergy/${BPN}/archive/refs/tags/v${PV}.tar.gz \
    file://fixvirtual-switch-allow-array-of-strings-for-dropdown.patch \
    file://remove-husky-for-venus.patch \
    file://0001-fix-switch-various-node-status-text-fixes.patch \
    file://0002-fix-services-restore-missing-mode-both-paths-from-v1.patch \
    file://npm-shrinkwrap.json;subdir=${S} \
"
INSANE_SKIP += "src-uri-bad"
PR = "1"

# Careful! When updating the version, also npm-shrinkwrap.json must be updated
SRC_URI[sha256sum] = "f1e59e601e4f3c9b5fde2a6d680cc86569c02b309e2e38addb63cc8688f12387"

inherit npm-online-install

do_install:append() {
	rm -rf ${D}${nonarch_libdir}/node_modules/@victronenergy/${PN}/node_modules/put/test
}
