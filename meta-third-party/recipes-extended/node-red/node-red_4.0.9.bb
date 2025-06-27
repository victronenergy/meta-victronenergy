DESCRIPTION = "Node-RED"
HOMEPAGE = "http://nodered.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=014f1a23c3da49aa929b21a96808ab22"

SRC_URI = "\
    npm://registry.npmjs.org;package=${BPN};version=${PV} \
    file://npm-shrinkwrap.json;subdir=${S} \
"
SRC_URI[sha256sum] = "a93c606f3620b4acc4900a428ad76f389508b9113d1ccd13d6c908b6c95741a6"
S = "${WORKDIR}/npm"

RDEPENDS:${PN} = "nodejs-npm"

inherit npm-online-install

do_install:append() {
    # Remove hardware specific files
    rm ${D}${NPM_INSTALLDIR}${nonarch_libdir}/node_modules/${PN}/bin/node-red-pi
    rm ${D}${bindir}/node-red-pi

    # Remove all prebuilt bcrypt binaries not matching target architecture
    find ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/ -type d -name 'bcrypt-linux-*' \
        ! -name "bcrypt-linux-${TARGET_ARCH}-*" -exec rm -rf {} +

    find ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/node-red-admin/node_modules/ -type d -name 'bcrypt-linux-*' \
        ! -name "bcrypt-linux-${TARGET_ARCH}-*" -exec rm -rf {} +
}
