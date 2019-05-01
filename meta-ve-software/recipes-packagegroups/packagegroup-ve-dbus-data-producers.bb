DESCRIPTION = "dbus services which provide measurements / data on the dbus"

PR = "r5"

# To make sure all packages needed by vrmlogger, gui etc get correctly installed
# all these recipes need to RDEPEND on all services they listen too. And if another
# is added all recipes must then be altered to include the new one. By rdepending
# on this packagegroup instead there is only one location where new packages which
# provide data on the dbus need to be added.

PACKAGES = "packagegroup-ve-dbus-data-producers"
LICENSE = "MIT"

inherit packagegroup

# List of application which provide data on the dbus in a VBusItem interface.
# These are RRECOMMENDS so the are only included when available.

RRECOMMENDS_${PN} += " \
	can-bus-bms \
	dbus-bornay-windplus \
	dbus-cgwacs \
	dbus-fronius \
	dbus-modem \
	dbus-motordrive \
	dbus-qwacs \
	dbus-redflow \
	dbus-systemcalc-py \
	dbus-valence \
	dbus-vebus-to-pvinverter \
	gps-dbus \
	mk2-dbus \
	vecan-dbus \
	vedirect-interface \
"

RRECOMMENDS_${PN}_append_beaglebone += "\
	dbus-adc \
	dbus-digitalinputs \
"

RRECOMMENDS_${PN}_append_canvu500 += "\
	dbus-digitalinputs \
"

RRECOMMENDS_${PN}_append_nanopi += "\
	dbus-digitalinputs \
"

RRECOMMENDS_${PN}_append_raspberrypi2 += "\
	dbus-digitalinputs \
"
