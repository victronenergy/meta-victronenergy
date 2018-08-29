FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://mosquitto.conf \
"

INITSCRIPT_PARAMS = "remove"

MOSQUITTO_D = "/data/conf/mosquitto.d"

do_install_append() {
	install -d ${D}${sysconfdir}/mosquitto
	install -m 0644 ${WORKDIR}/mosquitto.conf ${D}${sysconfdir}/mosquitto
	echo "include_dir ${MOSQUITTO_D}" >> ${D}${sysconfdir}/mosquitto/mosquitto.conf
}

DAEMONTOOLS_SERVICE_DIR = "${sysconfdir}/mosquitto/service"
DAEMONTOOLS_SCRIPT = "mkdir -p ${MOSQUITTO_D} && exec softlimit -d 100000000 -s 1000000 -a 100000000 ${sbindir}/mosquitto -c ${sysconfdir}/mosquitto/mosquitto.conf"
DAEMONTOOLS_DOWN = "1"

inherit daemontools
