DESCRIPTION = "Creates the config files which are used runtime by Venus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
PR = "r3"

do_install_append() {
	conf=${D}${sysconfdir}/venus
	mkdir -p $conf

	echo ${MACHINE} > $conf/machine

	if [ -n "${@bb.utils.contains("MACHINE_FEATURES", "headless", "1", "", d)}" ]; then touch $conf/headless; fi

	# mk2/mk3 port. Used by vecan-dbus aka mk2-dbus, as well as mk2vsc and some more
	if [ -n "${VE_MKX_PORT}" ]; then echo ${VE_MKX_PORT} > $conf/mkx_port; fi

	# vedirect ports. Used by serialstarter script
	if [ -n "${VE_VEDIRECT_PORTS}" ]; then echo ${VE_VEDIRECT_PORTS} > $conf/vedirect_ports; fi

	# vedirect port which is also used as console port. Like on ccgx/bbp3. Used
	# by the serial starter script.
	if [ -n "${VE_VEDIRECT_AND_CONSOLE_PORT}" ]; then echo ${VE_VEDIRECT_AND_CONSOLE_PORT} >  $conf/vedirect_and_console_port; fi

	# gpio pins with a relay connected
	if [ -n "${VE_RELAYS}" ]; then echo ${VE_RELAYS} > $conf/relays; fi
}
