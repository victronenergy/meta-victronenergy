include gui-v2.inc

SRC_URI = " \
	https://github.com/nmbath/gui-v2/releases/download/v${PV}-branding/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}-branding.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "5beda370f2bccc070f571bb7f0ef90e47c52705760e8a2cfc9cf3725af599ed4"
S = "${UNPACKDIR}/wasm"

inherit localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-v2" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
