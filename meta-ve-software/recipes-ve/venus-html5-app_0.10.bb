DESCRIPTION = "Venus HTML5 app"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit www

RDEPENDS_${PN} = "hiawatha venus-html5-logger"

SRC_URI = " \
	https://github.com/victronenergy/venus-html5-app/releases/download/${PV}/venus-html5-app.tar.gz;downloadfilename=venus-html5-app-${PV}.tar.gz \
 \
"
SRC_URI[md5sum] = "03f16d5acdfd2faede2d6dfaa0cf327b"
SRC_URI[sha256sum] = "827f1ad4bb35aafed82ebd563d137ca4f5993b3c312537d6527d96ffa41451b3"

S = "${WORKDIR}"
BASE_DIR = "${WWW_ROOT}/app"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
	for f in $( find ${S}/dist -type f -printf "%P\n" ); do
		install -D "${S}/dist/$f" "${D}${BASE_DIR}/$f"
	done
}
