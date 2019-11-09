DESCRIPTION = "Creates the config files which are used runtime by Venus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit www

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += " \
    file://board-compat \
    file://canbus-config \
    file://get-unique-id \
    file://hw-revision \
    file://installation-name \
    file://product-id \
    file://product-name \
    file://machine-conf.sh \
"
SRC_URI_append_ccgx += "file://get-unique-id.c"
SRC_URI_append_einstein += "\
    file://backlight_device.in \
    file://blank_display_device.in \
"
SRC_URI_append_sunxi += "file://canbus_ports.in"

inherit update-rc.d

RDEPENDS_${PN} += "bash"

INITSCRIPT_NAME = "machine-conf.sh"
INITSCRIPT_PARAMS = "start 90 S ."

do_compile () {
    if [ -f ${WORKDIR}/get-unique-id.c ]; then
        ${CC} ${CFLAGS} ${LDFLAGS} \
            ${WORKDIR}/get-unique-id.c -o ${WORKDIR}/get-unique-id
    fi
}

CONF_FILES = "\
    SWU:swu-name \
    VE_BACKLIGHT:backlight_device \
    VE_BLANK_DISPLAY:blank_display_device \
    VE_BUZZER:buzzer \
    VE_CAN_PORTS:canbus_ports \
    VE_MKX_PORT:mkx_port \
    VE_PWM_BUZZER:pwm_buzzer \
    VE_VEDIRECT_AND_CONSOLE_PORT:vedirect_and_console_port \
    VE_VEDIRECT_PORTS:vedirect_ports \
"

python () {
    for v in [c.split(':')[0] for c in d.getVar('CONF_FILES').split()]:
        d.setVarFlag(v, "export", 1)
}

do_install_append() {
    conf=${D}${sysconfdir}/venus
    mkdir -p $conf

    echo ${MACHINE} > $conf/machine

    if [ -n "${@bb.utils.contains("MACHINE_FEATURES", "headless", "1", "", d)}" ]; then touch $conf/headless; fi

    for cf in ${CONF_FILES}; do
        name=${cf%:*}
        file=${cf#*:}

        if [ -e ${WORKDIR}/${file}.in ]; then
            install -m 644 ${WORKDIR}/${file}.in ${conf}
        else
            eval val=\${${name}}
            if [ -n "${val}" ]; then
                echo "${val}" >${conf}/${file}
            fi
        fi
    done

    install -d ${D}/${base_sbindir}
    install -m 755 ${WORKDIR}/get-unique-id ${D}/${base_sbindir}

    install -d ${D}/${bindir}
    install -m 755 ${WORKDIR}/board-compat ${D}/${bindir}
    install -m 755 ${WORKDIR}/canbus-config ${D}/${bindir}
    install -m 755 ${WORKDIR}/hw-revision ${D}/${bindir}
    install -m 755 ${WORKDIR}/installation-name ${D}/${bindir}
    install -m 755 ${WORKDIR}/product-id ${D}/${bindir}
    install -m 755 ${WORKDIR}/product-name ${D}/${bindir}

    install -d ${D}${WWW_ROOT}/venus
    ln -s /data/venus/unique-id ${D}${WWW_ROOT}/venus
    ln -s /opt/victronenergy/version ${D}${WWW_ROOT}/venus

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/machine-conf.sh ${D}${sysconfdir}/init.d
}
