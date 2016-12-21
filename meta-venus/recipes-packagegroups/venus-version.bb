PR = "${DISTRO_VERSION}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
S = "${WORKDIR}"

inherit ve_package
SRC_URI = " \
	file://postupgrade.sh \
	\
"

do_install () {
	printf "${DISTRO_VERSION}\n${DISTRO_NAME}\n${BUILDNAME}" > version
	install -d ${D}${vedir}
	install -m 644 version ${D}${vedir}

	install -d ${D}/${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/postupgrade.sh ${D}/${sysconfdir}/init.d
}

do_install[vardeps] += "DATETIME"
do_install[nostamp] = "1"
do_install[vardepsexclude] = "BUILDNAME"

pkg_postinst_${PN}_venus () {
	if [ "x$D" = "x" ]; then
		ln -s ${sysconfdir}/init.d/postupgrade.sh ${sysconfdir}/rc5.d/S25postupgrade
	fi
}

FILES_${PN} += "${vedir}"
