DESCRIPTION = "Scripts to run before a webserver"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
	file://www-overrides \
	file://ssl-certificate \
"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

do_install() {
	install -d "${D}/etc/venus/www.d"
	install -m 755 "${UNPACKDIR}/ssl-certificate" "${D}/etc/venus/www.d"
	install -m 755 "${UNPACKDIR}/www-overrides" "${D}/etc/venus/www.d"
}
