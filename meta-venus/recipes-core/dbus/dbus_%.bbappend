FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

VELIB_DEFAULT_DIRS = "1"
inherit ve_package

# note: PRINC is needed in danny/ccgx
PRINC ?= "666000"
PRINC := "${@int(PRINC) + 5}"

SRC_URI += " \
	file://dbus-daemon-watch.sh \
	file://system.conf \
 "

RDEPENDS_${PN}_venus = "inotify-tools"

# For our own images, allow access to the system dbus and guard the dbus process.
# The guard is there since the OOM-killer likes to kill the dbus process itself,
# instead of the processes causing the queues in it.
do_install_append_venus_class-target() {
    install ${WORKDIR}/dbus-daemon-watch.sh ${D}${bindir}/dbus-daemon-watch.sh
    install -m 0644 ${WORKDIR}/system.conf ${D}${sysconfdir}/dbus-1/system.conf

    # note: libexecdir differs between danny and yethro, hence sed it..
    sed -i -e "s:@libexecdir@:${libexecdir}:" ${D}${sysconfdir}/dbus-1/system.conf

    lsdir=${permanentlocalstatedir}/lib/dbus
    rm -rf ${D}${localstatedir}/lib/dbus
    ln -s ${lsdir} ${D}${localstatedir}/lib/dbus
    echo "d messagebus messagebus 0755 ${lsdir} none" \
        >> ${D}${sysconfdir}/default/volatiles/99_dbus
}
