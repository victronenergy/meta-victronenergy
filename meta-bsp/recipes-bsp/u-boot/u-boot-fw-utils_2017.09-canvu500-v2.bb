require u-boot-canvu500.inc

SRC_URI += " \
	file://fw_env.config \
"

PROVIDES = ""

do_compile () {
	oe_runmake ${UBOOT_MACHINE}
	oe_runmake CC="${CC} ${CFLAGS} ${LDFLAGS}" envtools
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
