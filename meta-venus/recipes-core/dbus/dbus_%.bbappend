FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# note: PRINC is needed in danny/ccgx
PRINC ?= "666000"
PRINC := "${@int(PRINC) + 4}"

SRC_URI += " \
	file://dbus-daemon-watch.sh \
	file://system.conf \
 "

RDEPENDS_${PN}_venus = "inotify-tools"

# For our own images, allow access to the system dbus and guard the dbus process.
# The guard is there since the OOM-killer likes to kill the dbus process itself,
# instead of the processes causing the queues in it.
do_install_append_venus() {
    install ${WORKDIR}/dbus-daemon-watch.sh ${D}${bindir}/dbus-daemon-watch.sh
    install -m 0644 ${WORKDIR}/system.conf ${D}${sysconfdir}/dbus-1/system.conf
}
