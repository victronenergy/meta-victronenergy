DESCRIPTION = "Venus HTML5 app"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit www

RDEPENDS_${PN} = "hiawatha venus-html5-logger"

SRC_URI = " \
	https://github.com/victronenergy/venus-html5-app/releases/download/${PV}/venus-html5-app.tar.gz \
"
SRC_URI[md5sum] = "72a30417d4734b1c1cf83e14f4df4d9a"
SRC_URI[sha256sum] = "5a6e648160b10a083da39d5fc711710764b561345a5ad5a27b941608aa21a51e"

S = "${WORKDIR}"
BASE_DIR = "${WWW_ROOT}/app"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
	for f in $( find ${S}/dist -type f -printf "%P\n" ); do
		install -D "${S}/dist/$f" "${D}${BASE_DIR}/$f"
	done
}
