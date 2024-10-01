FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://dbus-daemon-watch.sh \
    file://localsettings \
    file://system.conf \
    file://system-insecure.conf \
"

RDEPENDS:${PN}:class-target += "inotify-tools xmlstarlet"
INITSCRIPT_PARAMS = "start 02 5 4 3 2 . stop 20 0 1 6 ."

inherit localsettings

# For our own images, allow access to the system dbus and guard the dbus process.
# The guard is there since the OOM-killer likes to kill the dbus process itself,
# instead of the processes causing the queues in it.
do_install:append:venus() {
    install ${UNPACKDIR}/dbus-daemon-watch.sh ${D}${bindir}/dbus-daemon-watch.sh
    install -m 0644 ${UNPACKDIR}/system.conf ${D}${sysconfdir}/dbus-1/system.conf

    # NOTE: only for debugging / fun. This is really not secure!
    install -m 0644 ${UNPACKDIR}/system-insecure.conf ${D}${sysconfdir}/dbus-1/system-insecure.conf

    # note: libexecdir differs between danny and yethro, hence sed it..
    sed -i -e "s:@libexecdir@:${libexecdir}:" ${D}${sysconfdir}/dbus-1/system.conf
}
