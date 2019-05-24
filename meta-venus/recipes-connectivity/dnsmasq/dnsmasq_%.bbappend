FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

DEPENDS += "update-rc.d-native"

SRC_URI += "\
	file://0001-Don-t-spam-syslog-on-reload-and-not-using-hosts-file.patch \
	file://0002-Log-only-changes-to-nameserver-list.patch \
	file://dnsmasq.ap.conf \
	file://resolv.conf \
	file://resolv-watch \
	file://rw-init \
"

hostap = ""
hostap_beaglebone = "1"
hostap_nanopi = "1"

RW_INITSCRIPT_PARAMS="start 80 5 . stop 10 0 1 6 ."

do_install_append() {
	install -m 644 ${WORKDIR}/resolv.conf ${D}${sysconfdir}

	install -d ${D}${sbindir}
	install -m 755 ${WORKDIR}/resolv-watch ${D}${sbindir}
	install -m 755 ${WORKDIR}/rw-init ${D}${sysconfdir}/init.d/resolv-watch
	update-rc.d -r ${D} resolv-watch ${RW_INITSCRIPT_PARAMS}

	if [ -n "${hostap}" ]; then
		install -m 644 ${WORKDIR}/dnsmasq.ap.conf ${D}${sysconfdir}
		ln -s dnsmasq ${D}${sysconfdir}/init.d/dnsmasq.ap
		update-rc.d -r ${D} dnsmasq.ap ${INITSCRIPT_PARAMS}
	fi
}
