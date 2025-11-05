DESCRIPTION = "VE character display server for easysolar"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit gmakevelib
inherit daemontools
inherit python-compile

# omitted since machine-runtime is machine specific.
# RDEPENDS:${PN} = "machine-runtime-conf"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-characterdisplay.git;branch=master;protocol=https \
    file://start-characterdisplay.sh \
"
SRCREV = "35fb7f1db71bd05add5c38856d1fe8262b367075"

S = "${WORKDIR}/git"

RDEPENDS:${PN} = " \
    dbus-systemcalc-py \
    localsettings \
    python3-core \
    python3-dbus \
    python3-evdev \
"

DAEMONTOOLS_RUN = "${@softlimit(d, data=100000000, stack=1000000, all=100000000)} ${bindir}/start-characterdisplay.sh"

do_install:append () {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/start-characterdisplay.sh ${D}${bindir}
}
