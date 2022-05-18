DESCRIPTION = "Node-RED"
HOMEPAGE = "http://nodered.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=014f1a23c3da49aa929b21a96808ab22"

SRC_URI = "\
    npm://registry.npmjs.org;package=${PN};version=${PV} \
    file://npm-shrinkwrap.json;subdir=${S} \
"
SRC_URI[sha256sum] = "e840fa1c7d7b25b0565551ad3582e24214cefb772a9af0238a9f7dac94f4dabb"
S = "${WORKDIR}/npm"

RDEPENDS:${PN} = "nodejs-npm"

inherit npm-online-install

do_install:append() {
    # Remove hardware specific files
    rm ${D}${NPM_INSTALLDIR}${nonarch_libdir}/node_modules/${PN}/bin/node-red-pi
    rm ${D}${bindir}/node-red-pi

    rm ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/bcrypt/build-tmp-napi-v3/Release/nothing.a
    rm ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/bcrypt/build-tmp-napi-v3/Release/node-addon-api/nothing.a
}
