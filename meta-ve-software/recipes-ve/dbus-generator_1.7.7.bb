LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5788eada6cf655ecefcba0bcac5add84"

inherit ve_package
inherit daemontools
inherit python-compile

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/dbus_generator.git;branch=master;protocol=https \
    file://com.victronenergy.generator-starter.conf \
"
SRCREV = "e92e637d0dac8a71371c2a2009273783f8b5495f"
S = "${WORKDIR}/git"

RDEPENDS:${PN} = " \
    localsettings \
    python3-core \
    python3-ctypes \
    python3-datetime \
    python3-dbus \
    python3-json \
    python3-pygobject \
"

DAEMONTOOLS_RUN = "${@softlimit(d, data=100000000, stack=1000000, all=100000000)} ${bindir}/dbus_generator.py"
DAEMONTOOLS_DOWN = "1"

do_install () {
    install -d ${D}${bindir}
    cp -r ${S}/* ${D}${bindir}

    install -d ${D}/${sysconfdir}/dbus-1/system.d
    install -m 644 ${UNPACKDIR}/com.victronenergy.generator-starter.conf ${D}/${sysconfdir}/dbus-1/system.d
}
