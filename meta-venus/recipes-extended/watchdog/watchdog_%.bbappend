inherit update-rc.d

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
	file://get_boot_type.c \
	file://init \
	file://watchdog.conf \
	file://store_watchdog_error.sh \
	file://0001-Use-MemAvailable-instead-of-MemFree.patch \
"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "watchdog"
INITSCRIPT_PARAMS_${PN} = "start 80 S . stop 20 0 1 6 ."

do_compile_append () {
	${CC} ${CFLAGS} ${LDFLAGS} -DMACH_${MACHINE} \
		${WORKDIR}/get_boot_type.c -o get_boot_type
}

do_install_append () {
	install -d ${D}${sysconfdir}/init.d ${D}${sbindir}
	install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/watchdog
	install -m 0755 ${B}/get_boot_type ${D}${sbindir}
	install -m 0755 ${WORKDIR}/store_watchdog_error.sh ${D}${sbindir}
	install -m 0644 ${WORKDIR}/watchdog.conf ${D}${sysconfdir}
}

