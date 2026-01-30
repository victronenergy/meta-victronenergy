require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc
require u-boot-k3.inc

PROVIDES = ""
SRC_URI += "file://fw_env.config"

CVE_PRODUCT = "u-boot-fwutils"

do_compile () {
    oe_runmake -C ${S} ${UBOOT_MACHINE}
    oe_runmake -C ${S} CC="${CC} ${CFLAGS} ${LDFLAGS}" STRIP="echo" envtools
    ${AR} rcs ${S}/tools/env/libubootenv.a $(${AR} t ${S}/tools/env/lib.a)
}

do_install () {
    install -d ${D}/${base_sbindir}
    install ${S}/tools/env/fw_printenv ${D}/${base_sbindir}
    ln -sf fw_printenv ${D}${base_sbindir}/fw_setenv

    install -d ${D}${libdir}
    install -m 644 ${S}/tools/env/libubootenv.a ${D}${libdir}

    install -d ${D}${sysconfdir}
    install -m 644 ${UNPACKDIR}/fw_env.config ${D}${sysconfdir}
}

do_deploy[noexec] = "1"

FILES:${PN} = "${base_sbindir} ${datadir} ${libdir} ${sysconfdir}"
