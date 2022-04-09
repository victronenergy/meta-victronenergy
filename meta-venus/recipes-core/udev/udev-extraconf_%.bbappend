FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += " \
    file://mount.sh \
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
    file://slcan.rules \
    file://wlan.rules \
    file://wlan-rename \
    file://wlan-update \
"

do_install:append() {
    install -m 0755 ${WORKDIR}/mount.sh ${D}${sysconfdir}/udev/scripts

    install -d ${D}/${sysconfdir}/udev/mount.blacklist.d
    install -m 0644 ${WORKDIR}/mount.blacklist.machine \
        ${D}/${sysconfdir}/udev/mount.blacklist.d/machine

    install -m 0755 -d ${D}${base_libdir}/udev
    install -m 0755 ${WORKDIR}/bt-config ${D}${base_libdir}/udev
    install -m 0755 ${WORKDIR}/bt-remove ${D}${base_libdir}/udev
    install -m 0755 ${WORKDIR}/display-hotplug ${D}${base_libdir}/udev

    install -m 0644 ${WORKDIR}/bluetooth.rules ${D}/${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/ether.rules ${D}/${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/machine.rules ${D}/${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/mtd.rules ${D}/${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/rfkill.rules ${D}/${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/simcom.rules ${D}/${sysconfdir}/udev/rules.d
}

do_install:append:beaglebone() {
    install -m 0644 ${WORKDIR}/wlan.rules ${D}${sysconfdir}/udev/rules.d

    install -m 0755 ${WORKDIR}/wlan-rename ${D}${base_libdir}/udev
    install -m 0755 ${WORKDIR}/wlan-update ${D}${base_libdir}/udev
}

do_install:append:ccgx() {
    install -m 0644 ${WORKDIR}/rtl8192cu.rules ${D}${sysconfdir}/udev/rules.d
}

do_install:append:einstein() {
    install -m 0755 ${WORKDIR}/sunxi-losc-status ${D}${base_libdir}/udev
}

do_install:append:sunxi() {
    install -m 0644 ${WORKDIR}/slcan.rules ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/wlan.rules ${D}${sysconfdir}/udev/rules.d

    install -m 0755 ${WORKDIR}/wlan-rename ${D}${base_libdir}/udev
    install -m 0755 ${WORKDIR}/wlan-update ${D}${base_libdir}/udev
}

FILES:${PN} += "${base_libdir}"
