include gui-v2.inc

SRC_URI = " \
	https://github.com/victronenergy/gui-v2/releases/download/v${PV}/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "882da093e9a908daea2c415d9fbc126bb0d4bc4b991946149e821ba23542cd89"

# Container support is not yet available in an official GUIv2 release.
SRC_URI:venus-container = " \
	https://github.com/nmbath/gui-v2/releases/download/v1.4.0-OCI/venus-webassembly.zip;downloadfilename=venus-webassembly-v1.4.0-OCI.zip;name=venuscontaineroci \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[venuscontaineroci.sha256sum] = "47300ecc48ba29d413a0459acd4c63a63990b35eee4b8d7ce22bb9c332cf7fe0"

S = "${UNPACKDIR}/wasm"

inherit localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-v2" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
