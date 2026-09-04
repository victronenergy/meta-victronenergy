include gui-v2.inc

CONTAINERS_REV = "4"
SRC_URI = " \
	https://github.com/nmbath/gui-v2/releases/download/v${PV}-container-${CONTAINERS_REV}/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}-container-${CONTAINERS_REV}.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "bc38c94fc86fb3fb4864f04d7034962ee8f0317fc533f2625b0ae5a2f5608b74"
S = "${UNPACKDIR}/wasm"

inherit localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-v2" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
