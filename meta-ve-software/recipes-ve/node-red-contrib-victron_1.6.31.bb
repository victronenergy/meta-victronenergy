SUMMARY = "Victron Venus D-Bus plugin for node-red"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d7725b8b5e691085738d564afb312302"

SRC_URI = " \
    https://github.com/victronenergy/${BPN}/archive/refs/tags/v${PV}.tar.gz \
    file://0001-Fix-bug-in-sending-initial-value.patch \
    file://0001-fix-ensure-initial-value-sent.patch \
    file://0003-Reconnect-to-the-dbus-quicker.patch \
    file://0004-Resolve-killing-the-dbus-connection.patch \
    file://0005-fix-make-dbus-text-property-optional-WIP.patch \
    file://0006-Fix-null-text-crashing-node-red-when-setting-status-.patch \
    file://npm-shrinkwrap.json;subdir=${S} \
    file://0007-Fix-dbus-listener-getValue-use-original-destination-.patch \
"
PR = "3"
INSANE_SKIP += "src-uri-bad"

# Careful! When updating the version, also npm-shrinkwrap.json must be updated
SRC_URI[sha256sum] = "2bdc79f78f0c4618f5a11bbcfb0b80b19ff2a92075dd16e0ee649db9f1d44806"

inherit npm-online-install

do_install:append() {
	rm -rf ${D}${nonarch_libdir}/node_modules/@victronenergy/${PN}/node_modules/put/test
}
