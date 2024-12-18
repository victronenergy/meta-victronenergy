LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit gmakevelib
inherit daemontools-template
inherit python-compile

RDEPENDS:${PN} = " \
    flashmq \
    python3-core \
    python3-lxml \
    python3-paho-mqtt \
    python3-requests \
"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-mqtt.git;branch=master;protocol=https \
"
SRCREV = "00bbfd58d048b3ca805199955659f84f31754106"
S = "${WORKDIR}/git"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_mqtt.py --init-broker"
