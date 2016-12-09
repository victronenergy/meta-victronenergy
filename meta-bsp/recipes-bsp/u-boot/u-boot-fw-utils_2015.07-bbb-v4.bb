require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=0507cd7da8e7ad6d6701926ec9b84c95"

S = "${WORKDIR}/u-boot-${PV}"
PROVIDES = ""

SRC_URI = " \
	https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz \
	file://fw_env.config \
"
SRC_URI[md5sum] = "e5ee3a9c5c1338a8d5f685f74234fc51"
SRC_URI[sha256sum] = "ff43e6c4b6fdc6bf1054dff627fad7b8cb39de9fe7489a0df08fcc250c97835a"

do_compile () {
	oe_runmake ${UBOOT_MACHINE}
	oe_runmake CC="${CC} ${CFLAGS} ${LDFLAGS}" env
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
