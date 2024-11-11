DESCRIPTION = "Andri's Main Loop."
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6f3cfaa39204b96e14b68b9d50d3e4e"

inherit meson

SRC_URI = " \
    git://github.com/any1/aml;protocol=https;branch=master \
"
SRCREV = "b83f3576ce4187d9285f06e9066ef43a691464d4"
S = "${WORKDIR}/git"

