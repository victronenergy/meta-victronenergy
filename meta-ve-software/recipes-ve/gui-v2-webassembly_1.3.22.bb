include gui-v2.inc

SRC_URI = " \
	https://github.com/nmbath/gui-v2/releases/download/${PV}-containers/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}-containers.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "a1b9068f835bf25c95b89ef3b95648e85bf781b21eba49565086b915ed01de85"
S = "${UNPACKDIR}/wasm"

inherit localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-v2" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
