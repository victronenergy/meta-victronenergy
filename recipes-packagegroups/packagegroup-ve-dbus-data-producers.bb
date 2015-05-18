DESCRIPTION = "dbus services which provide measurements / data on the dbus"

PR = "r1"
PACKAGES = "packagegroup-ve-dbus-data-producers"
LICENSE = "MIT"

inherit packagegroup

# List of application which provide data on the dbus in a VBusItem interface.
# These are RRECOMMENDS so the are only included when available.
RRECOMMENDS_${PN} += " \
	dbus-fronius \
	dbus-motordrive \
	dbus-systemcalc-py \
	dbus-valence \
	dbus-vebus-to-pvinverter \
	gps-dbus \
	vecan-dbus \
	vedirect-interface \
"
