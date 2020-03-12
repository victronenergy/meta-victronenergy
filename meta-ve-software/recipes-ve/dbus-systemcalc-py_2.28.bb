DESCRIPTION = "VE system calculations"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit ve_package
inherit daemontools
inherit python-compile

SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-systemcalc-py.git;protocol=https;tag=${PV} \
    file://com.victronenergy.system.conf \
    file://0001-remove-old-Lynx-Ion-battery-from-battery-settings.patch \
    file://0002-don-t-crash-when-the-firmware-on-the-Multi-is-old.patch \
    file://0003-make-grid-alarm-more-robust.patch \
    file://0004-deal-with-multi-compact-that-shows-disconnected-when.patch \
    file://0005-allow-quirks-to-raise-the-charge-voltage.patch \
    file://0006-add-support-for-byd-premium-battery-productid.patch \
"

PR = "3"

S = "${WORKDIR}/git"

RDEPENDS_${PN} = " \
    localsettings \
    python \
    python-dbus \
    python-pprint \
    python-pygobject \
"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_systemcalc.py"

do_install () {
    install -d ${D}${bindir}
    cp -r ${S}/* ${D}${bindir}

    install -d ${D}/${sysconfdir}/dbus-1/system.d
    install -m 644 ${WORKDIR}/com.victronenergy.system.conf ${D}/${sysconfdir}/dbus-1/system.d
}
