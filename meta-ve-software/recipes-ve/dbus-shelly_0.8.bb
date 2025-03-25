LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=824610c0b9d5c83da2edb0fab490487f"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "gitsm://github.com/victronenergy/dbus-shelly;branch=master;protocol=https"
SRCREV = "8626a9538b8e4bec5b0765dcc068987057c601f4"
S = "${WORKDIR}/git"

inherit daemontools gmakevelib python-compile ve_package

RDEPENDS:${PN} = "python3-core python3-dbus-fast python3-websockets"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_shelly.py"
