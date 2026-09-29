include gui-v2.inc

SRC_URI = " \
	https://github.com/nmbath/gui-v2/releases/download/v${PV}-branding/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}-branding.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "30f4d035a80713145cd3806e3e4584a43c89e2ad9a84afca2f48fda3593661b7"
S = "${UNPACKDIR}/wasm"

inherit localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-v2" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
