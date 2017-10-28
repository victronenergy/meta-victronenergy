require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "nanopi"

PROVIDES = ""

SRC_URI = " \
	git://github.com/victronenergy/u-boot.git;protocol=https;branch=nanopi \
	file://fw_env.config \
	file://u-boot.env \
"
SRCREV = "7de8ad4372d15404069f5cf77f4a5418ab7f9c07"

S = "${WORKDIR}/git"

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

	install -d ${D}${datadir}/u-boot
	install -m 644 ${WORKDIR}/u-boot.env ${D}${datadir}/u-boot
}

do_deploy[noexec] = "1"

FILES_${PN} = "${base_sbindir} ${datadir} ${libdir} ${sysconfdir}"
