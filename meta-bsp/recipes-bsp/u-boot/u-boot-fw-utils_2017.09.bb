require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

SRC_URI += " \
	file://fw_env.config \
"

SRC_URI[md5sum] = "58c92bf2c46dc82f1b57817f09ca8bd8"
SRC_URI[sha256sum] = "37f7ffc75ec3c38ea3125350cc606d3ceac071ab68811c9fb0cfc25d70592e22"

# This revision corresponds to the tag "v2017.09"
# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "c98ac3487e413c71e5d36322ef3324b21c6f60f9"

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
