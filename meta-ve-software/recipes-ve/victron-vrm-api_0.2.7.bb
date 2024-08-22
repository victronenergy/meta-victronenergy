SUMMARY = "Victron VRM API node for Node-RED"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=74d8bd507d727660243d82b9c2a30ab4"

SRC_URI = " \
    https://github.com/dirkjanfaber/${PN}/archive/refs/tags/v${PV}.tar.gz \
    file://npm-shrinkwrap.json;subdir=${S} \
"

# Careful! When updating the version, also npm-shrinkwrap.json must be updated
SRC_URI[sha256sum] = "9af7201f5970ae4df020518f59847bb7de440175c77d3bb2142eeb88eb505b93"

inherit npm-online-install

do_install:append() {
}
