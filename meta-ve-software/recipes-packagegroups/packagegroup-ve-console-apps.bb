PR = "r0"
LICENSE = "MIT"

inherit packagegroup

DESCRIPTION = " \
	Victron Energy applications which can also be installed on a headless system. \
	As in not X / wayland depend, since not all targets have a X server \
"

RDEPENDS_${PN} += " \
	dbusrecorder \
	dbus-generator-starter \
	dbus-modbustcp \
	dbus-mqtt \
	dbus-pump \
	dbus-systemcalc-py \
	dbus-vebus-to-pvinverter \
	javascript-vnc-client \
	localsettings \
"


