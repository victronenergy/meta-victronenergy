include gui-v2.inc

SRC_URI = " \
	https://github.com/nmbath/gui-v2/releases/download/v${PV}-web-pages/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}-web-pages.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "ab144b4a8e950dd6f01180db2afbab4ef0dc126c17979f3b4d23f874a206c6d3"
S = "${UNPACKDIR}/wasm"

inherit localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-v2" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
