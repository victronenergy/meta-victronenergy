FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += " \
	file://mount.sh \
	file://dt-compatible \
	file://dt-compat.rules \
	file://ether.rules \
	file://machine.rules \
	file://rtl8192cu.rules \
	file://simcom.rules \
"

SRC_URI_append_beaglebone += "\
	file://mount.blacklist.beaglebone \
	file://wlan.rules \
	file://wlan-rename \
	file://wlan-update \
"

SRC_URI_append_nanopi += "\
	file://mount.blacklist.nanopi \
	file://slcan.rules \
	file://wlan.rules \
	file://wlan-rename \
	file://wlan-update \
"

SRC_URI_append_raspberrypi2 += "\
	file://mount.blacklist.raspberrypi2 \
"

do_install_append() {
	install -m 0755 ${WORKDIR}/mount.sh ${D}${sysconfdir}/udev/scripts

	if [ -e ${WORKDIR}/mount.blacklist.${MACHINE} ]; then
		install -d ${D}/${sysconfdir}/udev/mount.blacklist.d
		install -m 0644 ${WORKDIR}/mount.blacklist.${MACHINE} \
			${D}/${sysconfdir}/udev/mount.blacklist.d/${MACHINE}
	fi

	install -m 0755 -d ${D}${base_libdir}/udev
	install -m 0755 ${WORKDIR}/dt-compatible ${D}${base_libdir}/udev

	install -m 0644 ${WORKDIR}/dt-compat.rules ${D}/${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/ether.rules ${D}/${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/machine.rules ${D}/${sysconfdir}/udev/rules.d
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

do_install_append_nanopi() {
	install -m 0644 ${WORKDIR}/slcan.rules ${D}${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/wlan.rules ${D}${sysconfdir}/udev/rules.d

	install -m 0755 ${WORKDIR}/wlan-rename ${D}${base_libdir}/udev
	install -m 0755 ${WORKDIR}/wlan-update ${D}${base_libdir}/udev
}

FILES_${PN} += "${base_libdir}"
