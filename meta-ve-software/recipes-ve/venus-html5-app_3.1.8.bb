DESCRIPTION = "Venus HTML5 app"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit www
inherit localsettings

RDEPENDS:${PN} = "nginx venus-html5-logger"

SRC_URI = " \
    https://github.com/victronenergy/venus-html5-app/releases/download/${PV}/venus-html5-app.tar.gz;downloadfilename=venus-html5-app-${PV}.tar.gz \
    file://localsettings \
"
SRC_URI[sha256sum] = "0471352aec20c114af21afdfa7c3d9d886ea086a3d23c433e9c6b50db873ca08"

S = "${WORKDIR}"
BASE_DIR = "${WWW_ROOT}/default/app"

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    for f in $( find ${S}/www/app -type f -printf "%P\n" ); do
        install -D "${S}/www/app/$f" "${D}${BASE_DIR}/$f"
    done

    ln -s /run/www/app ${D}${WWW_ROOT}/app
}