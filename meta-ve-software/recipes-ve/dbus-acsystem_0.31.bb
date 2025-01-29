DESCRIPTION = " \
    This reads data from Multi-RS systems (and potentially also others infuture) and \
    combines/summarises data for three-phase (and later parallel) units that are part \
    of the same system. \
"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=db73e07aea31188dc97142d139df39f9"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "gitsm://github.com/victronenergy/${BPN}.git;protocol=https;branch=master"
SRCREV = "93317455f6d706f2979fa07101080e4ada85219f"
S = "${WORKDIR}/git"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/${PN}.py"

inherit daemontools gmakevelib python-compile

RDEPENDS:${PN} = " \
    localsettings \
    python3-asyncio \
    python3-core \
    python3-dbus-fast \
    python3-logging \
"
