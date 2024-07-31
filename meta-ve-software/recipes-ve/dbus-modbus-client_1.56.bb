DESCRIPTION = "Modbus device handler"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit ve_package
inherit daemontools
inherit python-compile

RDEPENDS:${PN} = "\
    bash \
    python3-core \
    python3-dbus \
    python3-dnslib \
    python3-pygobject \
    python3-pymodbus \
"

SRC_URI = " \
    gitsm://github.com/victronenergy/${BPN}.git;branch=master;protocol=https;tag=v${PV} \
    file://start-serial.sh \
"
S = "${WORKDIR}/git"

DAEMONTOOLS_RUN = "${bindir}/${PN}.py"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}"

DAEMONTOOLS_SERVICE_DIR_SERIAL = "${DAEMONTOOLS_COMMON_TEMPLATES_DIR}/${PN}.serial"
DAEMONTOOLS_RUN_SERIAL = "${bindir}/start-serial.sh TTY"
DAEMONTOOLS_LOG_DIR_SERIAL = "${DAEMONTOOLS_LOG_DIR}.TTY"

do_install () {
    oe_runmake DESTDIR=${D} install

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/start-serial.sh ${D}${bindir}

    SERVICE="${D}${DAEMONTOOLS_SERVICE_DIR_SERIAL}"

    install -d ${SERVICE}
    echo "#!/bin/sh" > ${SERVICE}/run
    echo "exec 2>&1" >> ${SERVICE}/run
    echo "exec ${DAEMONTOOLS_RUN_SERIAL}" >> ${SERVICE}/run
    chmod 755 ${SERVICE}/run
    touch ${SERVICE}/down

    install -d ${SERVICE}/log
    echo "#!/bin/sh" > ${SERVICE}/log/run
    echo "exec 2>&1" >> ${SERVICE}/log/run
    echo "exec multilog t s25000 n4 ${DAEMONTOOLS_LOG_DIR_SERIAL}" >> ${SERVICE}/log/run
    chmod 755 ${SERVICE}/log/run
}

FILES:${PN} += "${DAEMONTOOLS_SERVICE_DIR_SERIAL}"
