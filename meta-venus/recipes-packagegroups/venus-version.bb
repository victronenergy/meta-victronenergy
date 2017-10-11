PR = "${DISTRO_VERSION}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
S = "${WORKDIR}"

inherit ve_package

do_install () {
	printf "${DISTRO_VERSION}\n${DISTRO_NAME}\n${BUILDNAME}" > version
	install -d ${D}${vedir}
	install -m 644 version ${D}${vedir}
}

do_install[vardeps] += "DATETIME"
do_install[nostamp] = "1"
do_install[vardepsexclude] = "BUILDNAME"

FILES_${PN} += "${vedir}"
