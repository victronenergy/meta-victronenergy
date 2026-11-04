DESCRIPTION = "VE system Dynamic ESS"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5d5b72f0438f97f350694bf9e756ffdf"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "gitsm://github.com/victronenergy/venus-dynamicess.git;branch=main;protocol=https"
SRCREV = "be0a9db0e7bc88c133f1fdb75e5e0d655188b67f"
S = "${WORKDIR}/git"

inherit daemontools-template python-compile ve_package

DAEMONTOOLS_RUN = "${@softlimit(d, data=100000000, stack=1000000, all=100000000)} ${bindir}/dynamicess.py"

do_install() {
    oe_runmake install DESTDIR=${D} bindir=${bindir}
}

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

