# note: swupdate wants to have the default env, so use the same source as u-boot itself!
require u-boot-venus.inc

# Sync the default environment with the newer U-Boot version used on Raspberry Pi 5
SRC_URI:append:raspberrypi5 = " file://0001-include-configs-rpi.h-Raspberry-Pi-5.patch"

SUMMARY = "U-Boot bootloader fw_printenv/setenv utilities"
DEPENDS += "mtd-utils"

SRC_URI:append = " file://fw_env.config"

INSANE_SKIP:${PN} += "already-stripped"
EXTRA_OEMAKE:class-target = 'CROSS_COMPILE=${TARGET_PREFIX} CC="${CC} ${CFLAGS} ${LDFLAGS}" HOSTCC="${BUILD_CC} ${BUILD_CFLAGS} ${BUILD_LDFLAGS}" V=1'
EXTRA_OEMAKE:class-cross = 'HOSTCC="${CC} ${CFLAGS} ${LDFLAGS}" V=1'

inherit uboot-config

do_compile () {
    oe_runmake -C ${S} ${UBOOT_MACHINE}
    oe_runmake -C ${S} envtools
}

do_install () {
    install -d ${D}${base_sbindir}
    install -d ${D}${sysconfdir}
    install -m 755 ${S}/tools/env/fw_printenv ${D}${base_sbindir}/fw_printenv
    install -m 755 ${S}/tools/env/fw_printenv ${D}${base_sbindir}/fw_setenv

    #install -m 0644 ${S}/tools/env/fw_env.config ${D}${sysconfdir}/fw_env.config
    install -m 0644 ${UNPACKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config

    install -d ${D}${libdir}
    install -m 644 ${S}/tools/env/libubootenv.a ${D}${libdir}/libubootenv.a
}

do_install:class-cross () {
    install -d ${D}${bindir_cross}
    install -m 755 ${S}/tools/env/fw_printenv ${D}${bindir_cross}/fw_printenv
    install -m 755 ${S}/tools/env/fw_printenv ${D}${bindir_cross}/fw_setenv
}

SYSROOT_DIRS:append:class-cross = " ${bindir_cross}"

PACKAGE_ARCH = "${MACHINE_ARCH}"
BBCLASSEXTEND = "cross"
