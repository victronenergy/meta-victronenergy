DESCRIPTION = "dbus services which provide measurements / data on the dbus"

PR = "r5"
PACKAGES = "packagegroup-ve-dbus-data-producers"
LICENSE = "MIT"

inherit packagegroup

# List of application which provide data on the dbus in a VBusItem interface.
# These are RRECOMMENDS so the are only included when available.
RRECOMMENDS_${PN} += " \
	dbus-cgwacs \
	dbus-fronius \
	dbus-motordrive \
	dbus-qwacs \
	dbus-redflow \
	dbus-systemcalc-py \
	dbus-valence \
	dbus-vebus-to-pvinverter \
	gps-dbus \
	lg-resu-interface \
	vecan-dbus \
	vedirect-interface \
"
