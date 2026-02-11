LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=824610c0b9d5c83da2edb0fab490487f"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "gitsm://github.com/victronenergy/dbus-shelly;branch=master;protocol=https"
SRCREV = "6262717e15bcbcb47e38098c724686effc823315"
S = "${WORKDIR}/git"

inherit daemontools gmakevelib python-compile useradd ve_package

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "shelly"
USERADD_PARAM:${PN} = "--no-create-home --shell /bin/false -g shelly shelly"

RDEPENDS:${PN} = " \
    python3-aiohttp \
    python3-aioshelly \
    python3-asyncio \
    python3-core \
    python3-dbus-fast \
    python3-image \
    python3-websockets \
    python3-zeroconf \
"

DAEMONTOOLS_RUN = "setuidgid shelly ${@softlimit(d, data=100000000, stack=1000000, all=100000000)} ${bindir}/dbus_shelly.py"
