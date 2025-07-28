DESCRIPTION = "GSM/3G modem manager"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit ve_package
inherit daemontools
inherit python-compile

RDEPENDS:${PN} = "\
    bash \
    python3-core \
    python3-dbus \
    python3-pygobject \
    python3-pyserial \
"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-modem.git;branch=master;protocol=https \
    file://start-modem.sh \
    file://reset-modem.sh \
"
SRCREV = "34223c78746470cea78de4d7a3592200c1d3e8c2"
S = "${WORKDIR}/git"

DAEMONTOOLS_RUN = "${bindir}/start-modem.sh TTY"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}.TTY"
DAEMONTOOLS_SERVICE_SYMLINK = "0"
DAEMONTOOLS_DOWN = "1"
DAEMONTOOLS_SERVICE_DIR = "${DAEMONTOOLS_TEMPLATE_DIR}"

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/dbus-modem.py ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/start-modem.sh ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/reset-modem.sh ${D}${bindir}

    for f in settingsdevice ve_utils vedbus; do
        install -m 0644 ${S}/ext/velib_python/$f.py ${D}${bindir}
    done
}
