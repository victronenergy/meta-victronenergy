SUMMARY = "U-Boot environment tools"
DESCRIPTION = "install fw_setenv and fw_printenv"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"

COMPATIBLE_MACHINE = "ccgx"

SRC_URI += "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "5523944a5f4595f9e22c861247b727f2"
SRC_URI[sha256sum] = "4c4e1158909be0a2b01f8f8c06eedecd1a3be10cd7279c5a73f87859b6ce7c62"
S = "${WORKDIR}/u-boot-${PV}"

inherit autotools

SRC_URI += " \
    file://fw_env.config \
    file://u-boot.env \
"

do_compile () {
    oe_runmake -C ${S} ${UBOOT_MACHINE}
    oe_runmake -C ${S} HOSTCC="${CC} ${CFLAGS} ${LDFLAGS}" HOSTSTRIP="echo" env
}

do_install () {
    install -d ${D}${sysconfdir} ${D}/${bindir}

    install ${S}/tools/env/fw_printenv ${D}/${bindir}
    ln -sf ${bindir}/fw_printenv ${D}${bindir}/fw_setenv

    install ${WORKDIR}/fw_env.config ${D}${sysconfdir}

    # swupdate needs this to modify the u-boot env
    install -d ${D}${libdir}
    install -m 644 ${S}/tools/env/libubootenv.a ${D}${libdir}

    mkdir -p ${D}${datadir}/u-boot
    install ${WORKDIR}/u-boot.env ${D}${datadir}/u-boot
}

FILES_${PN} += "${datadir}"
