DESCRIPTION = "VE system calculations"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit ve_package
inherit daemontools
inherit python-compile

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-systemcalc-py.git;branch=master;protocol=https \
    file://com.victronenergy.system.conf \
"
SRCREV = "b8f6ae2894be488d33a96756a101d209b985cb3f"
S = "${WORKDIR}/git"

RDEPENDS:${PN} = " \
    localsettings \
    python3-core \
    python3-dbus \
    python3-pprint \
    python3-pygobject \
"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_systemcalc.py"

do_install () {
    install -d ${D}${bindir}
    cp -r ${S}/* ${D}${bindir}

    install -d ${D}/${sysconfdir}/dbus-1/system.d
    install -m 644 ${UNPACKDIR}/com.victronenergy.system.conf ${D}/${sysconfdir}/dbus-1/system.d
}
