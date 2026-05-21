DESCRIPTION = "Signal K"
HOMEPAGE = "https://signalk.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34f8c1142fd6208a8be89399cb521df9"

DEPENDS += "avahi"
RDEPENDS:${PN} += "bash jq nodejs-npm util-linux-setpriv"

SRC_URI = "\
    npm://registry.npmjs.org;package=${BPN};version=${PV} \
    file://0001-package.json-no-more-optional-packages-signalk-venus.patch \
    file://0002-remove-signalk-server-setup-script.patch \
    file://0003-package.json-add-socketcan-package.patch \
    file://npm-shrinkwrap.json;subdir=${S} \
    file://canbus.json \
    file://defaults.json \
    file://get-mfd-announce-address.sh \
    file://logo.svg \
    file://prepare-signalk.sh \
    file://settings.json \
    file://start-signalk.sh \
    file://signalk-n2kais-to-nmea0183.json \
    file://sk-to-nmea0183.json \
    file://venus.json \
"

SRC_URI[sha256sum] = "bcce4914cc53039b579d5357d2f853920a2cbc29df18a66ffdf1701a96a4bdd3"

S = "${UNPACKDIR}/npm"

inherit daemontools npm-online-install useradd

DAEMONTOOLS_SCRIPT = "${bindir}/prepare-signalk.sh && exec setpriv --init-groups --reuid signalk --regid signalk ${bindir}/start-signalk.sh"
DAEMONTOOLS_DOWN = "1"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-d /data/conf/signalk -r -p '*' -s /bin/false -G dialout signalk"

DEFAULTS = "${D}${nonarch_libdir}/node_modules/${PN}/defaults"

do_install:append() {
    # remove hardware specific files, fixes error like this:
    # ERROR: signalk-server-1.46.3-1 do_package_qa: QA Issue:
    # Architecture did not match (AArch64, expected ARM) on
    # /work/ [..] /prebuilds/linux-arm64/node.napi.armv8.node

    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/@serialport/bindings-cpp/prebuilds/android-arm
    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/@serialport/bindings-cpp/prebuilds/android-arm64
    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/@serialport/bindings-cpp/prebuilds/darwin-x64+arm64
    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/@serialport/bindings-cpp/prebuilds/linux-arm64
    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/@serialport/bindings-cpp/prebuilds/linux-arm
    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/@serialport/bindings-cpp/prebuilds/linux-x64
    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/@serialport/bindings-cpp/prebuilds/win32-ia32
    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/@serialport/bindings-cpp/prebuilds/win32-x64
	for i in fs url os
	do
		rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/bare-${i}/prebuilds/android-ia32/bare-${i}.bare
		rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/bare-${i}/prebuilds/android-x64/bare-${i}.bare
		rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/bare-${i}/prebuilds/android-arm64/bare-${i}.bare
		rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/bare-${i}/prebuilds/android-arm/bare-${i}.bare
		rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/bare-${i}/prebuilds/linux-arm64/bare-${i}.bare
		rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/bare-${i}/prebuilds/linux-x64/bare-${i}.bare
	done

    # this directory keeps the default settings. start-signalk.sh copies them
    # to the data partition on first boot.
    install -d ${DEFAULTS}
    install -m 0644 ${UNPACKDIR}/canbus.json ${DEFAULTS}
    install -m 0644 ${UNPACKDIR}/defaults.json ${DEFAULTS}
    install -m 0644 ${UNPACKDIR}/logo.svg ${DEFAULTS}
    install -m 0644 ${UNPACKDIR}/settings.json ${DEFAULTS}
    install -m 0644 ${UNPACKDIR}/signalk-n2kais-to-nmea0183.json ${DEFAULTS}
    install -m 0644 ${UNPACKDIR}/sk-to-nmea0183.json ${DEFAULTS}
    install -m 0644 ${UNPACKDIR}/venus.json ${DEFAULTS}
    install -m 0755 ${UNPACKDIR}/get-mfd-announce-address.sh ${D}${nonarch_libdir}/node_modules/${PN}/

    mkdir -p ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/prepare-signalk.sh ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/start-signalk.sh ${D}${bindir}

    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/put/test
    rm -rf ${D}/usr/node_modules

    find "${D}${nonarch_libdir}" -depth -type d -name "examples" -exec rm -rf {} \;
    find "${D}${nonarch_libdir}" -depth -type d -name "samples" -exec rm -rf {} \;
    find "${D}${nonarch_libdir}" -depth -type d -name "__pycache__" -exec rm -rf {} \;

	 # Remove source maps - only useful for development debugging
    find "${D}${nonarch_libdir}" -name "*.map" -delete

	# TypeScript declarations - not used by Node.js at runtime
    find "${D}${nonarch_libdir}" -name "*.d.ts" -delete

    # Binaryen bytecode
    find "${D}${nonarch_libdir}" -name "*.bc.js" -delete

    # Demo/example images
    find "${D}${nonarch_libdir}" -name "*-demo.gif" -delete
    find "${D}${nonarch_libdir}" -name "example.png" -delete

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
