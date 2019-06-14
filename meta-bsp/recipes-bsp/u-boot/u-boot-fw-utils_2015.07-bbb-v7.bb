require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=0507cd7da8e7ad6d6701926ec9b84c95"

COMPATIBLE_MACHINE = "beaglebone"

S = "${WORKDIR}/u-boot-${PV}"
PROVIDES = ""

SRC_URI = " \
	https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz \
	file://fw_env.config \
"
SRC_URI[md5sum] = "1524766c623a229e984bc97a7bdea2f0"
SRC_URI[sha256sum] = "c7dd35420d7925c1443a0001a64a35f56593683f93612537617a18d60300398c"

do_compile () {
	oe_runmake -C ${S} ${UBOOT_MACHINE}
	oe_runmake -C ${S} CC="${CC} ${CFLAGS} ${LDFLAGS}" env
}

do_install () {
	install -d ${D}/${base_sbindir}
	install ${S}/tools/env/fw_printenv ${D}/${base_sbindir}
	ln -sf fw_printenv ${D}${base_sbindir}/fw_setenv

	install -d ${D}${libdir}
	install -m 644 ${S}/tools/env/lib.a ${D}${libdir}/libubootenv.a

	install -d ${D}${sysconfdir}
	install -m 644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}
}

do_deploy[noexec] = "1"

FILES_${PN} = "${base_sbindir} ${libdir} ${sysconfdir}"
