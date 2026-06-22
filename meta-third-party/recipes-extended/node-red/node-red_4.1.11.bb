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
SRC_URI[sha256sum] = "3fe4c5de7c3bf90b8faeb7d4692b877f7d4515cd21a81cb0ce5a310e27afa6fa"
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

    # Remove source maps - only useful for development debugging
    find "${D}${nonarch_libdir}" -name "*.map" -delete

    # TypeScript declarations - not used by Node.js at runtime
    find "${D}${nonarch_libdir}" -name "*.d.ts" -delete

    # Binaryen bytecode
    find "${D}${nonarch_libdir}" -name "*.bc.js" -delete

    # Demo/example images
    find "${D}${nonarch_libdir}" -name "*-demo.gif" -delete
    find "${D}${nonarch_libdir}" -name "example.png" -delete

    # Docs
    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/docs

    # Browser-only JS bundles
    find "${D}${nonarch_libdir}" -name "ZSchema-browser*.js" -delete
    find "${D}${nonarch_libdir}" -path "*/rxjs/dist/bundles/rxjs.umd*.js" -delete
    find "${D}${nonarch_libdir}" -path "*/node-forge/dist/forge.all*.js" -delete
    find "${D}${nonarch_libdir}" -path "*/mathjs/lib/browser" -type d -exec rm -rf {} +

    # npm internal
    find "${D}${nonarch_libdir}" -name ".package-lock.json" -delete

    # Build tools - only used during development, not at runtime
    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/webpack
    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/terser

    # Moment.js browser bundles - Node.js uses the package main entry, not min/
    find "${D}${nonarch_libdir}" -path "*/moment/min/*" -delete

    # MQTT browser bundles (Node.js uses lib/ entry, not dist/)
    find "${D}${nonarch_libdir}" -path "*/mqtt/dist/mqtt.js" -delete
    find "${D}${nonarch_libdir}" -path "*/mqtt/dist/mqtt.min.js" -delete

    # Test directories (catches test fixtures missed by existing cleanup)
    find "${D}${nonarch_libdir}" -depth -type d -name "test" -exec rm -rf {} \;
    find "${D}${nonarch_libdir}" -depth -type d -name "__tests__" -exec rm -rf {} \;

    # C++ header files - only needed for native module compilation, not runtime
    find "${D}${nonarch_libdir}" -name "*.h" -path "*/node-addon-api/*" -delete

    # More browser-only bundles
    find "${D}${nonarch_libdir}" -name "ajv.bundle.js" -delete
    find "${D}${nonarch_libdir}" -path "*/node-forge/dist/forge.min.js" -delete
    find "${D}${nonarch_libdir}" -path "*/source-map/dist/source-map.debug.js" -delete
    find "${D}${nonarch_libdir}" -path "*/@js-temporal/polyfill/dist/index.umd.js" -delete

    # WebAssembly text format test files
    find "${D}${nonarch_libdir}" -name "*.wat" -delete

    # PDF documentation
    find "${D}${nonarch_libdir}" -name "*.pdf" -delete

    # Changelogs
    find "${D}${nonarch_libdir}" -name "CHANGELOG*" -delete
}
