DESCRIPTION = "Signal K"
HOMEPAGE = "https://signalk.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34f8c1142fd6208a8be89399cb521df9"

DEPENDS += "avahi"
RDEPENDS:${PN} += "bash jq nodejs-npm util-linux-setpriv"

SRC_URI = "\
    npm://registry.npmjs.org;package=${BPN};version=${PV} \
    file://0001-package.json-no-more-optional-packages-signalk-venus.patch \
    file://0002-remove-signalk-server-setup-script-its-largest-depen.patch \
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

SRC_URI[sha256sum] = "fff4f7d86ab3a1e581d337af230648cbbcbd208fc08ef7665a8ff99a179face3"

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
}
