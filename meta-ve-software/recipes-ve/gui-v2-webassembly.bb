include gui-v2.inc

SRC_URI = " \
	https://github.com/victronenergy/gui-v2/releases/download/v${PV}/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
	file://Makefile \
"
SRC_URI[sha256sum] = "${WASM_SHA256SUM}"
S = "${WORKDIR}/wasm"

inherit allarch localsettings www

do_install() {
    cp ${UNPACKDIR}/Makefile .
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-beta" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
    ln -s ${WWW_ROOT}/gui-beta ${D}/${WWW_ROOT}/gui-v2
}

RDEPENDS:${PN} += "bash"
