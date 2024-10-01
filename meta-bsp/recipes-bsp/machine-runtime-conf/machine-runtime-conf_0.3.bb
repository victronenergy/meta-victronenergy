DESCRIPTION = "Creates the config files which are used runtime by Venus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit www

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += " \
    file://bad-unique-id \
    file://board-compat \
    file://get-unique-id \
    file://hw-revision \
    file://installation-date \
    file://installation-name \
    file://product-id \
    file://product-name \
    file://machine-conf.sh \
    file://ve-hash-passwd.c \
    file://ve-is-passwd-set-by-default \
    file://ve-passwd-as-in-factory \
    file://ve-set-passwd \
    file://ve-set-passwd-to-pincode \
"
SRC_URI:append:ccgx = " file://get-unique-id.c"
SRC_URI:append:sunxi = "\
    file://backlight_device.in \
    file://blank_display_device.in \
    file://pwm_buzzer.in \
"

inherit update-rc.d

DEPENDS = "virtual/crypt"
RDEPENDS:${PN} += "bash python3-core"

INITSCRIPT_NAME = "machine-conf.sh"
INITSCRIPT_PARAMS = "start 90 S ."

do_compile () {
    if [ -f ${UNPACKDIR}/get-unique-id.c ]; then
        ${CC} ${CFLAGS} ${LDFLAGS} \
            ${UNPACKDIR}/get-unique-id.c -o ${UNPACKDIR}/get-unique-id
    fi
    ${CC} ${CFLAGS} ${LDFLAGS} ${UNPACKDIR}/ve-hash-passwd.c -o ${UNPACKDIR}/ve-hash-passwd -lcrypt
}

VE_LARGE_IMAGE_SUPPORT ?= "1"

CONF_FILES = "\
    SWU:swu-name \
    VE_BACKLIGHT:backlight_device \
    VE_BLANK_DISPLAY:blank_display_device \
    VE_BUZZER:buzzer \
    VE_LARGE_IMAGE_SUPPORT:large_image_support \
    VE_PWM_BUZZER:pwm_buzzer \
"

python () {
    for v in [c.split(':')[0] for c in d.getVar('CONF_FILES').split()]:
        d.setVarFlag(v, "export", 1)
}

do_install:append() {
    conf=${D}${sysconfdir}/venus
    mkdir -p $conf

    echo ${MACHINE} > $conf/machine

    if [ -n "${@bb.utils.contains("MACHINE_FEATURES", "headless", "1", "", d)}" ]; then touch $conf/headless; fi

    for cf in ${CONF_FILES}; do
        name=${cf%:*}
        file=${cf#*:}

        if [ -e ${UNPACKDIR}/${file}.in ]; then
            install -m 644 ${UNPACKDIR}/${file}.in ${conf}
            ln -s /run/venus/${file} ${conf}/${file}
        else
            eval val=\${${name}}
            if [ -n "${val}" ]; then
                printf "${val}\n" >${conf}/${file}
            fi
        fi
    done

    install -d ${D}/${base_sbindir}
    install -m 755 ${UNPACKDIR}/get-unique-id ${D}/${base_sbindir}
    install -m 755 ${UNPACKDIR}/ve-hash-passwd ${D}/${base_sbindir}
    install -m 755 ${UNPACKDIR}/ve-is-passwd-set-by-default ${D}/${base_sbindir}
    install -m 744 ${UNPACKDIR}/ve-passwd-as-in-factory ${D}/${base_sbindir}
    install -m 744 ${UNPACKDIR}/ve-set-passwd ${D}/${base_sbindir}
    install -m 744 ${UNPACKDIR}/ve-set-passwd-to-pincode ${D}/${base_sbindir}

    install -d ${D}/${bindir}
    install -m 755 ${UNPACKDIR}/bad-unique-id ${D}/${bindir}
    install -m 755 ${UNPACKDIR}/board-compat ${D}/${bindir}
    install -m 755 ${UNPACKDIR}/hw-revision ${D}/${bindir}
    install -m 755 ${UNPACKDIR}/installation-date ${D}/${bindir}
    install -m 755 ${UNPACKDIR}/installation-name ${D}/${bindir}
    install -m 755 ${UNPACKDIR}/product-id ${D}/${bindir}
    install -m 755 ${UNPACKDIR}/product-name ${D}/${bindir}

    install -d ${D}${WWW_ROOT}/venus
    ln -s /data/venus/unique-id ${D}${WWW_ROOT}/venus
    ln -s /opt/victronenergy/version ${D}${WWW_ROOT}/venus

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/machine-conf.sh ${D}${sysconfdir}/init.d
}
