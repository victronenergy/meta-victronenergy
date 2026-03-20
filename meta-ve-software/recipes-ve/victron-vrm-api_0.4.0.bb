SUMMARY = "Victron VRM API node for Node-RED"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=74d8bd507d727660243d82b9c2a30ab4"

SRC_URI = " \
    https://github.com/dirkjanfaber/${BPN}/archive/refs/tags/v${PV}.tar.gz \
    file://npm-shrinkwrap.json;subdir=${S} \
"
INSANE_SKIP += "src-uri-bad"

# Careful! When updating the version, also npm-shrinkwrap.json must be updated
SRC_URI[sha256sum] = "b172f13d2e25370b6802530dd3b66e1ac2cb16718f882548acaf8bbc3ab95ffe"

inherit npm-online-install
