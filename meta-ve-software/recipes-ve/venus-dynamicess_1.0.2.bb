DESCRIPTION = "VE system Dynamic ESS"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5d5b72f0438f97f350694bf9e756ffdf"

inherit ve_package
inherit daemontools
inherit python-compile

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/venus-dynamicess.git;branch=main;protocol=https \
"
SRCREV = "8e0996cd5cb293ed31db1c7f8bf603faa0ba3a69"
S = "${WORKDIR}/git"

RDEPENDS:${PN} = " \
    localsettings \
    python3-asyncio \
    python3-core \
    python3-datetime \
    python3-dbus-fast \
    python3-json \
    python3-logging \
    python3-s2 \
    python3-typing-extensions \
"

do_compile:prepend() {
    sed -i '/solar_overhead.py/d' ${S}/Makefile
}

do_install:append() {
    oe_runmake install DESTDIR=${D} bindir=${bindir}
}

DAEMONTOOLS_RUN = "${@softlimit(d, data=100000000, stack=1000000, all=100000000)} ${bindir}/dynamicess.py"

