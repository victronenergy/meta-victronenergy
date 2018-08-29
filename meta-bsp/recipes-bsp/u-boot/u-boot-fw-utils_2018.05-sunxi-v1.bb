require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

PROVIDES = ""

SRC_URI = " \
	https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz \
	file://fw_env.config \
"
SRC_URI[md5sum] = "dda33501ea5cef319700dca67c650ff6"
SRC_URI[sha256sum] = "89585c5d34d1858abd0b39993242a37fcd3eafb03037dbc7359e4366bf01ee4d"

S = "${WORKDIR}/u-boot-${PV}"

do_compile () {
	oe_runmake -C ${S} ${UBOOT_MACHINE}
	oe_runmake -C ${S} CC="${CC} ${CFLAGS} ${LDFLAGS}" envtools
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

FILES_${PN} = "${base_sbindir} ${datadir} ${libdir} ${sysconfdir}"
