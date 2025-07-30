include gui-v2.inc

SRC_URI = " \
	https://github.com/victronenergy/gui-v2/releases/download/v${PV}/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "3540ae1056a9031e4ed2bc19f45b3e46ccfd2b001a8778ab77bf0ec278f2716a"
S = "${WORKDIR}/wasm"

inherit allarch localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-beta" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
    ln -s ${WWW_ROOT}/gui-beta ${D}/${WWW_ROOT}/gui-v2
}

RDEPENDS:${PN} += "bash"
