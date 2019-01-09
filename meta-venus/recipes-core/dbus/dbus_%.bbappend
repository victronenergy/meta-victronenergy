FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://dbus-daemon-watch.sh \
	file://system.conf \
 "

RDEPENDS_${PN}_append_venus = " inotify-tools"
INITSCRIPT_PARAMS = "start 02 5 4 3 2 . stop 20 0 1 6 ."

# For our own images, allow access to the system dbus and guard the dbus process.
# The guard is there since the OOM-killer likes to kill the dbus process itself,
# instead of the processes causing the queues in it.
do_install_append_venus() {
    install ${WORKDIR}/dbus-daemon-watch.sh ${D}${bindir}/dbus-daemon-watch.sh
    install -m 0644 ${WORKDIR}/system.conf ${D}${sysconfdir}/dbus-1/system.conf

    # note: libexecdir differs between danny and yethro, hence sed it..
    sed -i -e "s:@libexecdir@:${libexecdir}:" ${D}${sysconfdir}/dbus-1/system.conf
}
