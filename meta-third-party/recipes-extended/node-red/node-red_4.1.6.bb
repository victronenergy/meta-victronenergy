DESCRIPTION = "Node-RED"
HOMEPAGE = "http://nodered.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=014f1a23c3da49aa929b21a96808ab22"

SRC_URI = "\
    npm://registry.npmjs.org;package=${BPN};version=${PV} \
	file://Disable-showing-update-notifaction-in-tour.patch;apply=no \
    file://npm-shrinkwrap.json;subdir=${S} \
"
SRC_URI[sha256sum] = "ed00d8333ecdb13df99dc37a1babc81c238ae3811edb7cdb2698beac3d1c6f4d"
S = "${UNPACKDIR}/npm"

RDEPENDS:${PN} = "nodejs-npm"

inherit npm-online-install

do_install:prepend() {
    # Apply patch to remove update notification from tour
    cd ${WORKDIR}
    patch -p1 < ${UNPACKDIR}/Disable-showing-update-notifaction-in-tour.patch || bbfatal "Failed to apply tour patch"
}

do_install:append() {
    # Remove hardware specific files
    rm ${D}${NPM_INSTALLDIR}${nonarch_libdir}/node_modules/${PN}/bin/node-red-pi
    rm ${D}${bindir}/node-red-pi
}
