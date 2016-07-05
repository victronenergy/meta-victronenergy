PR = "${DISTRO_VERSION}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
S = "${WORKDIR}"

inherit ve_package
SRC_URI = " \
	file://reboot-after-opkg-done.sh \
	file://postupgrade.sh \
	\
"
RREPLACES_${PN}_bpp3 = "color-control"
RCONFLICTS_${PN}_bpp3 = "color-control"

do_install () {
	echo -e "${DISTRO_VERSION}\n${DISTRO_NAME}\n${BUILDNAME}" > version
	install -d ${D}${vedir}
	install -m 644 version ${D}${vedir}

	install -d ${D}${vedir}/opkg-scripts
	install -m 755 ${WORKDIR}/reboot-after-opkg-done.sh ${D}${vedir}/opkg-scripts

	install -d ${D}/${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/postupgrade.sh ${D}/${sysconfdir}/init.d
}

do_install[vardeps] += "DATETIME"
do_install[nostamp] = "1"
do_install[vardepsexclude] = "BUILDNAME"

pkg_postinst_${PN}_venus () {
	if [ "x$D" = "x" ]; then
		ln -s ${sysconfdir}/init.d/postupgrade.sh ${sysconfdir}/rc5.d/S25postupgrade

		echo Starting reboot workaround
		${vedir}/opkg-scripts/reboot-after-opkg-done.sh &
	fi
}

FILES_${PN} += "${vedir}"
