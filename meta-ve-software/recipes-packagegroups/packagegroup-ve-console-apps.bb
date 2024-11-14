PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

DESCRIPTION = " \
    Victron Energy applications which can also be installed on a headless system. \
    As in not X / wayland depend, since not all targets have a X server \
"

RDEPENDS:${PN} += " \
    dbus-acsystem \
    dbus-generator \
    dbus-mqtt \
    dbus-pump \
    dbus-recorder \
    dbus-systemcalc-py \
    dbus-tempsensor-relay \
    dbus-vebus-to-pvinverter \
    javascript-vnc-client \
    localsettings \
"
