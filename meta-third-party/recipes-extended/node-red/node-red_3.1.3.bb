DESCRIPTION = "Node-RED"
HOMEPAGE = "http://nodered.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=014f1a23c3da49aa929b21a96808ab22"

SRC_URI = "\
    npm://registry.npmjs.org;package=${PN};version=${PV} \
    file://npm-shrinkwrap.json;subdir=${S} \
"
SRC_URI[sha256sum] = "98f0468fe78d98564473591f36f99b66ea4d67a519865e4ede6ec07f2cc293f4"
S = "${WORKDIR}/npm"

RDEPENDS:${PN} = "nodejs-npm"

inherit npm-online-install

do_install:append() {
    # Remove hardware specific files
    rm ${D}${NPM_INSTALLDIR}${nonarch_libdir}/node_modules/${PN}/bin/node-red-pi
    rm ${D}${bindir}/node-red-pi

    rm -r ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/bcrypt/build-tmp-napi-v3
    rm ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/node-red-admin/node_modules/bcrypt/node-addon-api/nothing.a
    rm -r ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/node-red-admin/node_modules/bcrypt/build-tmp-napi-v3
}
