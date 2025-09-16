DESCRIPTION = "Signal K"
HOMEPAGE = "https://signalk.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34f8c1142fd6208a8be89399cb521df9"

DEPENDS += "avahi"
RDEPENDS:${PN} += "bash nodejs-npm util-linux-setpriv"

SRC_URI = "\
    npm://registry.npmjs.org;package=${BPN};version=${PV} \
    file://0001-Revert-feat-enable-node-source-map-support.patch \
    file://0002-package.json-change-optional-to-mandatory-and-add-si.patch \
    file://0003-remove-signalk-server-setup-script-its-largest-depen.patch \
    file://0004-package.json-remove-typedoc-signalk-theme-devDepende.patch \
    file://0005-package.json-add-socketcan-package.patch \
    file://npm-shrinkwrap.json;subdir=${S} \
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

SRC_URI[sha256sum] = "4e938112cdedcc65230d9107596ba2cda923dfc88db302d27528109a96edf521"

S = "${UNPACKDIR}/npm"

inherit daemontools npm-online-install useradd

DAEMONTOOLS_SCRIPT = "${bindir}/prepare-signalk.sh && exec setpriv --init-groups --reuid signalk --regid signalk ${bindir}/start-signalk.sh"
DAEMONTOOLS_DOWN = "1"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-d /data/conf/signalk -r -p '*' -s /bin/false -G dialout signalk"

DEFAULTS = "${D}${nonarch_libdir}/node_modules/${PN}/defaults"

# This shouldn't be here, for some reason both the cerbogxs and the einstein will
# trigger a build, while both are einstein SOMs. Anyway, since those images will
# be merged soon, make it machine specific for now, so at least a parrallel build
# will succeed.
PACKAGE_ARCH = "${MACHINE_ARCH}"

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
    install -m 0644 ${WORKDIR}/defaults.json ${DEFAULTS}
    install -m 0644 ${WORKDIR}/logo.svg ${DEFAULTS}
    install -m 0644 ${WORKDIR}/settings.json ${DEFAULTS}
    install -m 0644 ${WORKDIR}/signalk-n2kais-to-nmea0183.json ${DEFAULTS}
    install -m 0644 ${WORKDIR}/sk-to-nmea0183.json ${DEFAULTS}
    install -m 0644 ${WORKDIR}/venus.json ${DEFAULTS}
    install -m 0755 ${WORKDIR}/get-mfd-announce-address.sh ${D}${nonarch_libdir}/node_modules/${PN}/

    mkdir -p ${D}${bindir}
    install -m 0755 ${WORKDIR}/prepare-signalk.sh ${D}${bindir}
    install -m 0755 ${WORKDIR}/start-signalk.sh ${D}${bindir}

    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/put/test
    rm -rf ${D}/usr/node_modules

    find "${D}${nonarch_libdir}" -depth -type d -name "examples" -exec rm -rf {} \;
    find "${D}${nonarch_libdir}" -depth -type d -name "samples" -exec rm -rf {} \;
    find "${D}${nonarch_libdir}" -depth -type d -name "__pycache__" -exec rm -rf {} \;
}
