SRC_URI += "file://qt-kms.conf"

PACKAGE_ARCH = "${MACHINE_ARCH}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

do_install_append() {
	mkdir ${D}/${sysconfdir}
	install ${WORKDIR}/qt-kms.conf ${D}/${sysconfdir}

	mkdir ${D}/${sysconfdir}/profile.d
	echo "export QT_QPA_PLATFORM=eglfs" > ${D}/${sysconfdir}/profile.d/qt
	echo "export QT_QPA_KMS_CONFIG=${sysconfdir}/qt-kms.conf" >> ${D}/${sysconfdir}/profile.d/qt
}

