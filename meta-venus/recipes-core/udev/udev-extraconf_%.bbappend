FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += " \
    file://mount.sh \
    file://mount.blacklist \
    file://mount.blacklist.machine \
    file://bluetooth.rules \
    file://bt-config \
    file://bt-remove \
    file://display-hotplug \
    file://ether.rules \
    file://machine.rules \
    file://mtd.rules \
    file://rfkill.rules \
    file://rtl8192cu.rules \
    file://simcom.rules \
"

SRC_URI:append:beaglebone = "\
    file://wlan.rules \
    file://wlan-rename \
    file://wlan-update \
"

SRC_URI:append:einstein = "\
    file://sunxi-losc-status \
"

SRC_URI:append:sunxi = "\
    file://wlan.rules \
    file://wlan-rename \
    file://wlan-update \
"

do_install:append() {
    install -m 0755 ${UNPACKDIR}/mount.sh ${D}${sysconfdir}/udev/scripts

    install -d ${D}/${sysconfdir}/udev/mount.blacklist.d
    install -m 0644 ${UNPACKDIR}/mount.blacklist \
        ${D}/${sysconfdir}/udev/mount.blacklist
    install -m 0644 ${UNPACKDIR}/mount.blacklist.machine \
        ${D}/${sysconfdir}/udev/mount.blacklist.d/machine

    install -m 0755 -d ${D}${base_libdir}/udev
    install -m 0755 ${UNPACKDIR}/bt-config ${D}${base_libdir}/udev
    install -m 0755 ${UNPACKDIR}/bt-remove ${D}${base_libdir}/udev
    install -m 0755 ${UNPACKDIR}/display-hotplug ${D}${base_libdir}/udev

    install -m 0644 ${UNPACKDIR}/bluetooth.rules ${D}/${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/ether.rules ${D}/${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/machine.rules ${D}/${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/mtd.rules ${D}/${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/rfkill.rules ${D}/${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/simcom.rules ${D}/${sysconfdir}/udev/rules.d

    # Remove ifup hotplug support, this is done unconditionally and causes problems.
    # Older OE version didn't never did this, and it is now done uncondtionally.
    # busybox doesn't like the --hotplug support and OE doesn't like a workaround.
    # So just remove it.
    rm ${D}/${sysconfdir}/udev/rules.d/autonet.rules
    rm ${D}/${sysconfdir}/udev/scripts/network.sh
}

do_install:append:beaglebone() {
    install -m 0644 ${UNPACKDIR}/wlan.rules ${D}${sysconfdir}/udev/rules.d

    install -m 0755 ${UNPACKDIR}/wlan-rename ${D}${base_libdir}/udev
    install -m 0755 ${UNPACKDIR}/wlan-update ${D}${base_libdir}/udev
}

do_install:append:ccgx() {
    install -m 0644 ${UNPACKDIR}/rtl8192cu.rules ${D}${sysconfdir}/udev/rules.d
}

do_install:append:einstein() {
    install -m 0755 ${UNPACKDIR}/sunxi-losc-status ${D}${base_libdir}/udev
}

do_install:append:sunxi() {
    install -m 0644 ${UNPACKDIR}/wlan.rules ${D}${sysconfdir}/udev/rules.d

    install -m 0755 ${UNPACKDIR}/wlan-rename ${D}${base_libdir}/udev
    install -m 0755 ${UNPACKDIR}/wlan-update ${D}${base_libdir}/udev
}

FILES:${PN} += "${base_libdir}"
