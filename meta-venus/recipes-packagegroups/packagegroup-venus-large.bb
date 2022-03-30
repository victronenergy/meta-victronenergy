SUMMARY = "Extra packages for large image"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = " \
    node-red-venus \
    signalk-server \
"

# npm needs this to update signalk-venus-plugin
RDEPENDS:${PN} += " \
    dbus-dev \
    packagegroup-core-buildessential \
"
