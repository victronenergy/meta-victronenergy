SUMMARY = "Upload custom logo"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://logo.php"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
	mkdir -p "${D}/var/www/javascript-vnc-client"
	install -m 644 ${WORKDIR}/logo.php "${D}/var/www/javascript-vnc-client"
}

