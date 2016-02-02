FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# note: PRINC is needed in danny/ccgx
PRINC ?= "666000"
PRINC := "${@int(PRINC) + 3}"

SRC_URI += " \
	file://dbus-daemon-watch.sh \
	file://system.conf \
 "

RDEPENDS_${PN} = "inotify-tools"

do_install_append() {
    install ${WORKDIR}/dbus-daemon-watch.sh ${D}${bindir}/dbus-daemon-watch.sh
}

do_install_append_bpp3() {
    install -m 0644 ${WORKDIR}/system.conf ${D}${sysconfdir}/dbus-1/system.conf
}
