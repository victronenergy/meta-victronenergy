PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

DESCRIPTION = " \
    Victron Energy applications which can also be installed on a headless system. \
    As in not X / wayland depend, since not all targets have a X server \
"

RDEPENDS:${PN} += " \
    dbus-generator-starter \
    dbus-mqtt \
    dbus-pump \
    dbus-recorder \
    dbus-systemcalc-py \
    dbus-tempsensor-relay \
    dbus-vebus-to-pvinverter \
    javascript-vnc-client \
    localsettings \
"

RDEPENDS:${PN}:append:nanopi = " \
    dbus-characterdisplay \
"

