DESCRIPTION = "Signal K"
HOMEPAGE = "https://signalk.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34f8c1142fd6208a8be89399cb521df9"

DEPENDS += "avahi"
RDEPENDS:${PN} += "bash nodejs-npm util-linux-setpriv"

SRC_URI = "\
    npm://registry.npmjs.org;package=${PN};version=${PV} \
    file://add-venus-os-preinstalled-plugins.patch \
    file://npm-shrinkwrap.json;subdir=${S} \
    file://defaults.json \
    file://logo.svg \
    file://prepare-signalk.sh \
    file://settings.json \
    file://start-signalk.sh \
    file://signalk-n2kais-to-nmea0183.json \
    file://sk-to-nmea0183.json \
    file://venus.json \
"

SRC_URI[sha256sum] = "f331f2c7a1cbc06a583e169661ee466c157f3065ee2bdef684c04d93c019b077"
S = "${WORKDIR}/npm"

inherit daemontools npm-online-install useradd

DAEMONTOOLS_SCRIPT = "${bindir}/prepare-signalk.sh && exec setpriv --init-groups --reuid signalk --regid signalk ${bindir}/start-signalk.sh"
DAEMONTOOLS_DOWN = "1"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-d /data/conf/signalk -r -p '*' -s /bin/false -G dialout signalk"

DEFAULTS = "${D}${nonarch_libdir}/node_modules/${PN}/defaults"

do_install:append() {
    # this folder keeps the default settings. start-signalk.sh copies them
    # to the data partition on first boot.
    install -d ${DEFAULTS}
    install -m 0644 ${WORKDIR}/defaults.json ${DEFAULTS}
    install -m 0644 ${WORKDIR}/logo.svg ${DEFAULTS}
    install -m 0644 ${WORKDIR}/settings.json ${DEFAULTS}
    install -m 0644 ${WORKDIR}/signalk-n2kais-to-nmea0183.json ${DEFAULTS}
    install -m 0644 ${WORKDIR}/sk-to-nmea0183.json ${DEFAULTS}
    install -m 0644 ${WORKDIR}/venus.json ${DEFAULTS}

    mkdir -p ${D}${bindir}
    install -m 0755 ${WORKDIR}/prepare-signalk.sh ${D}${bindir}
    install -m 0755 ${WORKDIR}/start-signalk.sh ${D}${bindir}

    rm -rf ${D}${nonarch_libdir}/node_modules/${PN}/node_modules/put/test
    rm -rf ${D}/usr/node_modules
}
