include gui-v2.inc

# Container support is not yet available in an official GUIv2 release.
SRC_URI = " \
	https://github.com/nmbath/gui-v2/releases/download/v1.4.1-container-gx1/venus-webassembly.zip;downloadfilename=venus-webassembly-v1.4.1-container-gx1.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "b28e149d4b0286aa7c5b6121f464715b449c4e3b5f590786aafb09f1fbf21b00"

S = "${UNPACKDIR}/wasm"

inherit localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-v2" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
