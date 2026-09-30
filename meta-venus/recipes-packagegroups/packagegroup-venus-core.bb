SUMMARY = "Venus OS shared by the hardware and OCI images"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

require packagegroup-venus-core.inc

RDEPENDS:${PN} += "\
    machine-runtime-conf \
    packagegroup-core-boot \
    packagegroup-ve-console-apps \
    venus-version \
"

# netmon provides a workaround for an problem solved years ago
# and it is a open issue if the package shouldn't be dropped.
# Since it requires quite some resources with the scarthgap
# python version, drop it for the devices with little memory.
NETMON = " netmon"
NETMON:ccgx = ""
NETMON:canvu500 = ""
RDEPENDS:${PN}:append = "${NETMON}"

DBUS_SHELLY = " dbus-shelly"
DBUS_SHELLY:ccgx = ""
RDEPENDS:${PN}:append = "${DBUS_SHELLY}"
