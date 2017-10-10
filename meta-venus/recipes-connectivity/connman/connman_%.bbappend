FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

INITSCRIPT_PARAMS = "start 70 5 2 3 4 . stop 22 0 1 6 ."

# do not adjust dirs, only import them
VELIB_DEFAULT_DIRS = "1"
inherit ve_package

SRC_URI += "\
            file://0001-Set-hwclock-when-time-is-decoded.patch \
            file://0002-Increase-NTP-poll-interval.patch \
            file://0003-gweb-Do-not-lookup-for-a-NULL-key-in-a-hash-table.patch \
            file://0004-wifi-make-max-connection-retries-configurable.patch \
            file://0001-service-Update-nameservers-and-timeservers-with-chan.patch \
            file://main.conf \
            file://connmand-watch.sh \
"

do_configure_append() {
	sed -i -e 's:\$(localstatedir)/lib:${permanentlocalstatedir}/lib:' ${B}/Makefile
}

do_install_append() {
	install -d ${D}${sysconfdir}/connman
	install -m 0644 ${WORKDIR}/main.conf ${D}${sysconfdir}/connman/main.conf
	install -m 0755 ${WORKDIR}/connmand-watch.sh ${D}${sbindir}/connmand-watch.sh
}
