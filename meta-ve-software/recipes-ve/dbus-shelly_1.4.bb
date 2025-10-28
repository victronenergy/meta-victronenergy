LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=824610c0b9d5c83da2edb0fab490487f"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "gitsm://github.com/victronenergy/dbus-shelly;branch=master;protocol=https"
SRCREV = "f4464c1c38dfe1be657b1a5042ffee7f4dde05fd"
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
    python3-websockets \
    python3-zeroconf \
"

DAEMONTOOLS_RUN = "setuidgid shelly softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_shelly.py"
