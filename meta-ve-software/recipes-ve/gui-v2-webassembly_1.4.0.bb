include gui-v2.inc

SRC_URI = " \
	https://github.com/nmbath/gui-v2/releases/download/v${PV}-exchange/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}-exchange.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "4afb6e65580e5febac20e7f14c2a9299a5127560e8624691c72f374e45cbb5e9"
S = "${UNPACKDIR}/wasm"

inherit localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-v2" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
