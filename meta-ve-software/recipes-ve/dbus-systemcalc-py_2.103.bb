DESCRIPTION = "VE system calculations"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit ve_package
inherit daemontools
inherit python-compile

SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-systemcalc-py.git;branch=master;protocol=https;tag=${PV} \
    file://com.victronenergy.system.conf \
    file://FreedomWON.patch \
    file://0001-display-external-control-state-w-managed-battery.patch \
"

S = "${WORKDIR}/git"
PR = "2"

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
    install -m 644 ${WORKDIR}/com.victronenergy.system.conf ${D}/${sysconfdir}/dbus-1/system.d
}
