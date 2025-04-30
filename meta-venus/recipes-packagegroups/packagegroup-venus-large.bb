SUMMARY = "Extra packages for large image"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = " \
    node-red-venus \
    signalk-server \
    sudo \
"

# npm needs packagegroup-core-buildessential to update signalk-venus-plugin.
#
# python3-compile is for the filecmp module, node-gyp depends on it and it
# is needed to install node-red-contrib-socketcan.
RDEPENDS:${PN} += " \
    dbus-dev \
    packagegroup-core-buildessential \
    python3-compile \
"
