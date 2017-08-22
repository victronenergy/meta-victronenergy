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
SRC_URI[md5sum] = "2fbe0aa0778d7343d74c513d5e219e56"
SRC_URI[sha256sum] = "40ae2388b1d8910219f02aef83b50004737c6f4b3d8383fe62fbe7beee72efc9"

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
