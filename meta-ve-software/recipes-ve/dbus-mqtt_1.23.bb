LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit gmakevelib
inherit daemontools
inherit python-compile

RDEPENDS_${PN} = " \
    mosquitto \
    python3-core \
    python3-lxml \
    python3-paho-mqtt \
    python3-requests \
"

SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-mqtt.git;protocol=https;tag=v${PV} \
"
S = "${WORKDIR}/git"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_mqtt.py --init-broker"
DAEMONTOOLS_DOWN = "1"
