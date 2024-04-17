LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=824610c0b9d5c83da2edb0fab490487f"

SRC_URI = "gitsm://github.com/victronenergy/dbus-shelly;branch=master;protocol=https;tag=v${PV}"
S = "${WORKDIR}/git"

inherit daemontools gmakevelib python-compile ve_package

RDEPENDS:${PN} = "python3-core python3-dbus-next python3-websockets"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_shelly.py"
