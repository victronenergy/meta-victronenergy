SUMMARY = "Victron VRM API node for Node-RED"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=74d8bd507d727660243d82b9c2a30ab4"

SRC_URI = " \
    https://github.com/dirkjanfaber/${BPN}/archive/refs/tags/v${PV}.tar.gz \
    file://npm-shrinkwrap.json;subdir=${S} \
"
INSANE_SKIP += "src-uri-bad"

# Careful! When updating the version, also npm-shrinkwrap.json must be updated
SRC_URI[sha256sum] = "3f73cd0270034f156d720a067f3e1f3232884478b61ec2ec16eb8f72f6ebed1f"

inherit npm-online-install
