DESCRIPTION = "Node-RED"
HOMEPAGE = "http://nodered.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=014f1a23c3da49aa929b21a96808ab22"

SRC_URI = "\
    npm://registry.npmjs.org;package=${PN};version=${PV} \
    file://npm-shrinkwrap.json;subdir=${S} \
"
SRC_URI[sha256sum] = "2babe48282a3cc9e9a40547e5ef0c99fcd1c77b7d2a40a12977a6559a8d29784"
S = "${WORKDIR}/npm"

RDEPENDS:${PN} = "nodejs-npm"

inherit npm-online-install

do_install:append() {
    # Remove hardware specific files
    rm ${D}${NPM_INSTALLDIR}${nonarch_libdir}/node_modules/${PN}/bin/node-red-pi
    rm ${D}${bindir}/node-red-pi

    rm -r ${D}${nonarch_libdir}/node_modules/node-red/node_modules/@node-rs/bcrypt-linux-x64-gnu
}
