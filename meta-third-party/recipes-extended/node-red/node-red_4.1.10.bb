DESCRIPTION = "Node-RED"
HOMEPAGE = "http://nodered.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=014f1a23c3da49aa929b21a96808ab22"

SRC_URI = "\
    npm://registry.npmjs.org;package=${BPN};version=${PV} \
    file://Disable-showing-update-notifaction-in-tour.patch;apply=no \
    file://Omit-peer-deps-from-palette-installs.patch;apply=no \
    file://npm-shrinkwrap.json;subdir=${S} \
"
SRC_URI[sha256sum] = "a91656cb5c97b98655540e0d557aeebbfad9a2134ac48c9d47eb3798803ae3d1"
S = "${UNPACKDIR}/npm"

RDEPENDS:${PN} = "nodejs-npm"

inherit npm-online-install

do_install:prepend() {
    # Apply patch to remove update notification from tour
    cd ${WORKDIR}
    patch -p1 < ${UNPACKDIR}/Disable-showing-update-notifaction-in-tour.patch || bbfatal "Failed to apply tour patch"
    patch -p1 < ${UNPACKDIR}/Omit-peer-deps-from-palette-installs.patch || bbfatal "Failed to apply omit-peer patch"
}

do_install:append() {
    # Remove hardware specific files
    rm ${D}${NPM_INSTALLDIR}${nonarch_libdir}/node_modules/${PN}/bin/node-red-pi
    rm ${D}${NPM_INSTALLDIR}${nonarch_libdir}/node_modules/node-red/node_modules/npm/lib/utils/completion.sh
    rm ${D}${bindir}/node-red-pi
}
