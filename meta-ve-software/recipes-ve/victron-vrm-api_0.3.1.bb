SUMMARY = "Victron VRM API node for Node-RED"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=74d8bd507d727660243d82b9c2a30ab4"

SRC_URI = " \
    https://github.com/dirkjanfaber/${BPN}/archive/refs/tags/v${PV}.tar.gz \
    file://npm-shrinkwrap.json;subdir=${S} \
"
INSANE_SKIP += "src-uri-bad"

# Careful! When updating the version, also npm-shrinkwrap.json must be updated
SRC_URI[sha256sum] = "447a7485fac26a948e5a7c7b31273d05ec5dcda2380a23ae447d704a0331f7b1"

inherit npm-online-install
