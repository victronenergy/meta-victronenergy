SUMMARY = "U-Boot environment tools"
DESCRIPTION = "install fw_setenv and fw_printenv"

# note, the fancy version number is needed to be newer than
# the git+hash version number from earlier version.

require u-boot-ccgx.src.inc

inherit autotools

SRC_URI += " \
	file://fw_env.config \
	file://u-boot.env \
"

do_compile () {
	oe_runmake -C ${S} ${UBOOT_MACHINE}
	oe_runmake -C ${S} HOSTCC="${CC}" HOSTSTRIP="echo" env
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
