SUMMARY = "U-Boot environment tools"
DESCRIPTION = "install fw_setenv and fw_printenv"

# note, the fancy version number is needed to be newer than
# the git+hash version number from earlier version.

require u-boot.src.inc
PR = "r0"

inherit autotools

SRC_URI += "file://fw_env.config"

do_compile () {
	oe_runmake bpp3_config
	oe_runmake HOSTCC="${CC}" HOSTSTRIP="echo" env
}

do_install () {
	install -d ${D}${sysconfdir} ${D}/${bindir}

	install ${S}/tools/env/fw_printenv ${D}/${bindir}
	ln -sf ${bindir}/fw_printenv ${D}${bindir}/fw_setenv

	install ${WORKDIR}/fw_env.config ${D}${sysconfdir}
}

