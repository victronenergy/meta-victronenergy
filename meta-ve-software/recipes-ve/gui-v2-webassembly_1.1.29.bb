include gui-v2.inc

SRC_URI = " \
	https://github.com/victronenergy/gui-v2/releases/download/v${PV}/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "ea6402a63a115bb9da1f271a1e08ed9aa387e3ad794105f6ada022810e7b093e"
S = "${WORKDIR}/wasm"

inherit allarch localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-beta" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
    ln -s ${WWW_ROOT}/gui-beta ${D}/${WWW_ROOT}/gui-v2
}

RDEPENDS:${PN} += "bash"
