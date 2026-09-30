include gui-v2.inc

# Container support is not yet available in an official GUIv2 release.
SRC_URI = " \
	https://github.com/nmbath/gui-v2/releases/download/v1.4.0-OCI2/venus-webassembly.zip;downloadfilename=venus-webassembly-v1.4.0-OCI2.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "e333cff36aaf6269bedb5b5346952f60f6d082e3f04e109a789c6e722d325efe"

S = "${UNPACKDIR}/wasm"

inherit localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-v2" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
