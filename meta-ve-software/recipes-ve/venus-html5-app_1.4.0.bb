DESCRIPTION = "Venus HTML5 app"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit www

RDEPENDS_${PN} = "hiawatha venus-html5-logger"

SRC_URI = " \
    https://github.com/victronenergy/venus-html5-app/releases/download/${PV}/venus-html5-app.tar.gz;downloadfilename=venus-html5-app-${PV}.tar.gz \
 \
"
SRC_URI[md5sum] = "49f0bc70063272e20f0b9614cb8be221"
SRC_URI[sha256sum] = "1d6b1b8cc8a66d9d2e6be1bb247d3d0a5758ca412dac286f23e528b0fbf0703e"

S = "${WORKDIR}"
BASE_DIR = "${WWW_ROOT}/app"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    for f in $( find ${S}/dist -type f -printf "%P\n" ); do
        install -D "${S}/dist/$f" "${D}${BASE_DIR}/$f"
    done
}
