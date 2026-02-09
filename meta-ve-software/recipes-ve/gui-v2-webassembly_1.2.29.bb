include gui-v2.inc

SRC_URI = " \
	https://github.com/victronenergy/gui-v2/releases/download/v${PV}/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "5ae2d4e928ad1dcc1edca0d040fcb4bb28f4501d08052fd0d28b933158947718"
S = "${UNPACKDIR}/wasm"

inherit localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-v2" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
