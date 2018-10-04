FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://avahi-daemon.conf"

do_install_append() {
	rm -f ${D}${sysconfdir}/avahi/services/*
	install -m 0644 ${WORKDIR}/avahi-daemon.conf ${D}${sysconfdir}/avahi
}
