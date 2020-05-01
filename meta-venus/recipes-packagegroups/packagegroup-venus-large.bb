SUMMARY = "Extra packages for large image"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup
LICENSE = "MIT"

RDEPENDS_${PN} = "\
    node-red \
    node-red-contrib-victron \
    nodejs-npm \
    packagegroup-core-buildessential \
    signalk-server \
"
