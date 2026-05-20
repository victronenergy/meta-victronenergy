SRC_URI = "gitsm://github.com/victronenergy/dbus-heatpump-control;protocol=https;branch=main"
DESCRIPTION = "Venus OS Heat Pump Control Service"
SRCREV = "5bab7ce5e118f9f80c43baccaefa125f4fa41b47"
S = "${WORKDIR}/git"
UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=876b44dded698736d08769eeb7c62258"

inherit daemontools gmakevelib python-compile ve_package

DAEMONTOOLS_RUN = "${@softlimit(d, data=100000000, stack=1000000, all=100000000)} ${bindir}/dbus-heatpump-control.py"

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

