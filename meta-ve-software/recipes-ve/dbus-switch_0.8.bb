SUMMARY = "Provides dbus support for relays and outputs on Venus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c3bad050ea665f85c545f72f62f6989b"

inherit daemontools-template gmakevelib python-compile ve_package

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "gitsm://github.com/victronenergy/dbus-switch.git;branch=main;protocol=https"

SRCREV = "a8de0da7841666837cc9c86681c053ab6593b4d7"
S = "${WORKDIR}/git"

RDEPENDS:${PN} = " \
    localsettings \
    python3-core \
    python3-dbus \
    python3-pygobject \
"

DAEMONTOOLS_RUN = "${bindir}/dbus-switch.py --serial SERIAL"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}.SERIAL"
DAEMONTOOLS_TEMPLATE_CONF = "PARAM = SERIAL: the hq number\n"
DAEMONTOOLS_TEMPLATE_CONF .= "SERVICE_EXT = .SERIAL\n"

