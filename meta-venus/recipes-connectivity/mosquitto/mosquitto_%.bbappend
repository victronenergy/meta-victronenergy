FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://mosquitto.conf \
    file://start-mosquitto \
"

INHIBIT_UPDATERCD_BBCLASS = "1"

MOSQUITTO_D = "/run/mosquitto"
PACKAGECONFIG = "ssl uuid websockets"

do_install_append() {
    install -m 0755 ${WORKDIR}/start-mosquitto ${D}${sbindir}

    install -d ${D}${sysconfdir}/mosquitto
    install -m 0644 ${WORKDIR}/mosquitto.conf ${D}${sysconfdir}/mosquitto
    echo "include_dir ${MOSQUITTO_D}" >> ${D}${sysconfdir}/mosquitto/mosquitto.conf

    install -d ${D}${sysconfdir}/default/volatiles
    echo "d root root 0755 ${MOSQUITTO_D} none" > ${D}${sysconfdir}/default/volatiles/50_${PN}
}

FILES_${PN} += "\
    ${sbindir}/start-mosquitto \
    ${sysconfdir}/default \
"

DAEMONTOOLS_SERVICE_DIR = "${sysconfdir}/mosquitto/service"
DAEMONTOOLS_RUN = "${sbindir}/start-mosquitto"
DAEMONTOOLS_DOWN = "1"

inherit daemontools
