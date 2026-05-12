include gui-v2.inc

SRC_URI = " \
	https://github.com/victronenergy/gui-v2/releases/download/v${PV}/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "020c8f093ac59883141a30e7c9240960dcc4b23f1b78c356d5da66f32e1b1013"
S = "${UNPACKDIR}/wasm"

inherit localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-v2" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
