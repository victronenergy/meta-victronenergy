SUMMARY = "Provides dbus support for relays and outputs on Venus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c3bad050ea665f85c545f72f62f6989b"

inherit ve_package
inherit daemontools
inherit python-compile

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "\
    gitsm://github.com/victronenergy/dbus-switch.git;branch=main;protocol=ssh;user=git \
    file://start-dbus-switch.sh \
"

SRCREV = "f0a6679d21c7b91fd59e04949f547380a5fb8973"
S = "${WORKDIR}/git"

RDEPENDS:${PN} = " \
    localsettings \
    python3-core \
    python3-dbus \
    python3-pygobject \
"

DAEMONTOOLS_RUN = "${bindir}/start-dbus-switch.sh SERIAL"
DAEMONTOOLS_SERVICE_SYMLINK = "0"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}.SERIAL"
DAEMONTOOLS_SERVICE_DIR = "${DAEMONTOOLS_TEMPLATE_DIR}"
DAEMONTOOLS_TEMPLATE_CONF = " \
    PARAM = SERIAL: \
    SERVICE_EXT = .SERIAL \
"

do_install () {
    install -d ${D}${bindir}
    cp -r ${S}/* ${D}${bindir}

    install -m 0755 ${UNPACKDIR}/start-dbus-switch.sh ${D}${bindir}
}
