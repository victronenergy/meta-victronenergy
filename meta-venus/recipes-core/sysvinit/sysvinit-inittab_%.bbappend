FILESEXTRAPATHS_prepend := "${THISDIR}/sysvinit-inittab:"

SRC_URI += "file://autologin"

do_install_append () {
	install -d ${D}/${base_sbindir}
	install -m 0755 ${WORKDIR}/autologin ${D}/${base_sbindir}
}

FILES_${PN} += "${base_sbindir}"
