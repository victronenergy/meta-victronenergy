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
SRC_URI[sha256sum] = "642668dea7031852ac9ba474aa64558e685d3f93f9eeabfc5e4e89a4802d7ce6"

S = "${WORKDIR}"
BASE_DIR = "${WWW_ROOT}/default/app"

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    for f in $( find ${S}/dist -type f -printf "%P\n" ); do
        install -D "${S}/dist/$f" "${D}${BASE_DIR}/$f"
    done

    ln -s /run/www/app ${D}${WWW_ROOT}/app
}
