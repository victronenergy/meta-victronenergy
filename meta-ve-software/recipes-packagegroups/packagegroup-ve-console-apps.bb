PR = "r0"
LICENSE = "MIT"

inherit packagegroup

RDEPENDS_${PN} += " \
	dbusrecorder \
	dbus-generator-starter \
	dbus-modbustcp \
	dbus-systemcalc-py \
	dbus-vebus-to-pvinverter \
	javascript-vnc-client \
	localsettings \
	simple-upnpd \
"


