FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += " \
	file://mount.sh \
	file://mount.blacklist.machine \
	file://bluetooth.rules \
	file://bt-config \
	file://ether.rules \
	file://machine.rules \
	file://mtd.rules \
	file://rtl8192cu.rules \
	file://simcom.rules \
"

SRC_URI_append_beaglebone += "\
	file://wlan.rules \
	file://wlan-rename \
	file://wlan-update \
"

SRC_URI_append_sunxi += "\
	file://slcan.rules \
	file://wlan.rules \
	file://wlan-rename \
	file://wlan-update \
"

do_install_append() {
	install -m 0755 ${WORKDIR}/mount.sh ${D}${sysconfdir}/udev/scripts

	install -d ${D}/${sysconfdir}/udev/mount.blacklist.d
	install -m 0644 ${WORKDIR}/mount.blacklist.machine \
		${D}/${sysconfdir}/udev/mount.blacklist.d/machine

	install -m 0755 -d ${D}${base_libdir}/udev
	install -m 0755 ${WORKDIR}/bt-config ${D}${base_libdir}/udev

#	install -m 0644 ${WORKDIR}/bluetooth.rules ${D}/${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/ether.rules ${D}/${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/machine.rules ${D}/${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/mtd.rules ${D}/${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/simcom.rules ${D}/${sysconfdir}/udev/rules.d
}

do_install_append_beaglebone() {
	install -m 0644 ${WORKDIR}/wlan.rules ${D}${sysconfdir}/udev/rules.d

	install -m 0755 ${WORKDIR}/wlan-rename ${D}${base_libdir}/udev
	install -m 0755 ${WORKDIR}/wlan-update ${D}${base_libdir}/udev
}

do_install_append_ccgx() {
	install -m 0644 ${WORKDIR}/rtl8192cu.rules ${D}${sysconfdir}/udev/rules.d
}

do_install_append_sunxi() {
	install -m 0644 ${WORKDIR}/slcan.rules ${D}${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/wlan.rules ${D}${sysconfdir}/udev/rules.d

	install -m 0755 ${WORKDIR}/wlan-rename ${D}${base_libdir}/udev
	install -m 0755 ${WORKDIR}/wlan-update ${D}${base_libdir}/udev
}

FILES_${PN} += "${base_libdir}"
