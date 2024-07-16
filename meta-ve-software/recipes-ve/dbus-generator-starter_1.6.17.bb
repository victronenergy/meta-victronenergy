LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5788eada6cf655ecefcba0bcac5add84"

inherit ve_package
inherit daemontools
inherit python-compile

SRC_URI = " \
    gitsm://github.com/victronenergy/dbus_generator.git;branch=master;protocol=https;tag=v${PV} \
    file://com.victronenergy.generator-starter.conf \
"
S = "${WORKDIR}/git"
PR = "1"

RDEPENDS:${PN} = " \
    localsettings \
    python3-core \
    python3-ctypes \
    python3-datetime \
    python3-dbus \
    python3-json \
    python3-pygobject \
"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_generator.py"

do_install () {
    install -d ${D}${bindir}
    cp -r ${S}/* ${D}${bindir}

    install -d ${D}/${sysconfdir}/dbus-1/system.d
    install -m 644 ${WORKDIR}/com.victronenergy.generator-starter.conf ${D}/${sysconfdir}/dbus-1/system.d
}
