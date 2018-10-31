DESCRIPTION = "Venus HTML5 app"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit www

RDEPENDS_${PN} = "hiawatha"

SRC_URI = " \
	gitsm://github.com/victronenergy/${PN}.git;protocol=https;tag=${PV} \
"

S = "${WORKDIR}/git"
BASE_DIR = "${WWW_ROOT}/app"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
	install -d ${D}${BASE_DIR}
	cp -a ${S}/dist/* ${D}${BASE_DIR}
}
