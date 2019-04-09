DESCRIPTION = "Venus HTML5 app"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit www

RDEPENDS_${PN} = "hiawatha venus-html5-logger"

SRC_URI = " \
	https://github.com/victronenergy/venus-html5-app/releases/download/${PV}/venus-html5-app.tar.gz;downloadfilename=venus-html5-app-${PV}.tar.gz \
 \
"
SRC_URI[md5sum] = "19ae66a8b87ec8b048c45366097c2a5d"
SRC_URI[sha256sum] = "c4719d94c9c0c64346711c14ee9fb3754df955c22701c6853b86bdd74da3c5b8"

S = "${WORKDIR}"
BASE_DIR = "${WWW_ROOT}/app"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
	for f in $( find ${S}/dist -type f -printf "%P\n" ); do
		install -D "${S}/dist/$f" "${D}${BASE_DIR}/$f"
	done
}
