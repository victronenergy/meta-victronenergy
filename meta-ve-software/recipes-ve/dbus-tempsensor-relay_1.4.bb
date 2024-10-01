LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1fc534b12b7f08e16e57c04dd0364f53"

inherit ve_package
inherit daemontools
inherit python-compile
inherit gmakevelib

SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-tempsensor-relay.git;branch=master;protocol=https;tag=v${PV} \
    file://com.victronenergy.temprelay.conf \
"
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
