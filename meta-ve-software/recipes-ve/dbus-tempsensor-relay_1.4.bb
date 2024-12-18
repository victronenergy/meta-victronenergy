LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1fc534b12b7f08e16e57c04dd0364f53"

inherit ve_package
inherit daemontools
inherit python-compile
inherit gmakevelib

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-tempsensor-relay.git;branch=master;protocol=https \
    file://com.victronenergy.temprelay.conf \
"
SRCREV = "8830e0539470b1f3dc175600204559fd6ee0b8f6"
S = "${WORKDIR}/git"

RDEPENDS:${PN} = " \
    localsettings \
    python3-core \
    python3-datetime \
    python3-dbus \
    python3-pygobject \
"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_tempsensor_relay.py"
DAEMONTOOLS_DOWN = "1"

do_install:append () {
    install -d ${D}/${sysconfdir}/dbus-1/system.d
    install -m 644 ${UNPACKDIR}/com.victronenergy.temprelay.conf ${D}/${sysconfdir}/dbus-1/system.d
}
