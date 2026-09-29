DESCRIPTION = "Venus HTML5 app"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit www
inherit localsettings

RDEPENDS:${PN} = "nginx venus-html5-logger"

SRC_URI = " \
    https://github.com/victronenergy/venus-html5-app/releases/download/${PV}/venus-html5-app-${PV}.tar.gz;downloadfilename=venus-html5-app-${PV}.tar.gz;subdir=${S} \
    file://localsettings \
"
SRC_URI[sha256sum] = "5aa33e0d1c6a44d4c529b97913bddff4da965298e5382d92d6bf88b45586ca9c"
S = "${UNPACKDIR}/www"

BASE_DIR = "${WWW_ROOT}/default/app"

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    rm -f "${S}/dist/static/media/javascript,__webpack_public_path__ = __webpack_base_uri__ = htmlWebpackPluginPublicPath;.1feff74f.bin"

    for f in $( find ${S}/dist -type f -printf "%P\n" ); do
        install -D "${S}/dist/$f" "${D}${BASE_DIR}/$f"
    done

    ln -s /run/www/app ${D}${WWW_ROOT}/app
}
