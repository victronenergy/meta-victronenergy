PR = "r${DISTRO_VERSION}.0"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit ve_package

do_install () {
	echo -e "${DISTRO_VERSION}\n${DISTRO_NAME}\n${BUILDNAME}" > version
	install -d ${D}${vedir}
	install -m 644 version ${D}${vedir}
}

FILES_${PN} += "${vedir}"
