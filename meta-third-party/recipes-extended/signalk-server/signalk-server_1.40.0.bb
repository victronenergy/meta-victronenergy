DESCRIPTION = "Signal K"
HOMEPAGE = "https://signalk.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34f8c1142fd6208a8be89399cb521df9"

RDEPENDS:${PN} += "bash nodejs-npm"

SRC_URI = "\
    npm://registry.npmjs.org;package=${PN};version=${PV} \
    file://npm-shrinkwrap.json;subdir=${S} \
    file://defaults.json \
    file://logo.svg \
    file://prepare-signalk.sh \
    file://settings.json \
    file://start-signalk.sh \
    file://venus.json \
"
SRC_URI[sha256sum] = "dce512ccce4f5ddc35aa9799240f180977f1c48592ccd69f7634e296ac91ad54"
S = "${WORKDIR}/npm"

inherit daemontools npm-online-install useradd

DAEMONTOOLS_SCRIPT = "${bindir}/prepare-signalk.sh && exec setuidgid signalk ${bindir}/start-signalk.sh"
DAEMONTOOLS_DOWN = "1"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-d /data/conf/signalk -r -p '*' -s /bin/false -G dialout signalk"

do_compile:append() {
    npm install \
        --prefix="${NPM_BUILD}" \
        --arch=${NPM_ARCH} \
        --target_arch=${NPM_ARCH} \
        --production \
        --no-bin-links \
        signalk-venus-plugin@1.25
}

DEFAULTS = "${D}${nonarch_libdir}/node_modules/${PN}/defaults"

do_install:append() {
    # this folder keeps the default settings. start-signalk.sh copies them
    # to the data partition on first boot.
    install -d ${DEFAULTS}
    install -m 0644 ${WORKDIR}/defaults.json ${DEFAULTS}
    install -m 0644 ${WORKDIR}/logo.svg ${DEFAULTS}
    install -m 0644 ${WORKDIR}/settings.json ${DEFAULTS}
    install -m 0644 ${WORKDIR}/venus.json ${DEFAULTS}

    mkdir -p ${D}${bindir}
    install -m 0755 ${WORKDIR}/prepare-signalk.sh ${D}${bindir}
    install -m 0755 ${WORKDIR}/start-signalk.sh ${D}${bindir}

    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/put/test
    rm -rf ${D}/usr/node_modules
}
