PR = "${DISTRO_VERSION}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
S = "${WORKDIR}"

inherit ve_package

do_configure () {
	printf "${DISTRO_VERSION}\n${DISTRO_NAME}\n${BUILDNAME}\n" > version
}

do_configure[nostamp] = "1"
do_configure[vardepsexclude] = "BUILDNAME"

do_install () {
	install -d ${D}${vedir}
	install -m 644 version ${D}${vedir}
}

FILES_${PN} += "${vedir}"
