include gui-v2.inc

SRC_URI = " \
	http://updates.victronenergy.com/feeds/venus-gui-v2/venus-webassembly-454db4f9a62f520da49a330fda2cca8566d91d39.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "81b372d342507b36e61ff7469341d8c829301142d7201e936ba4aa5a65f49c92"
S = "${WORKDIR}/wasm"

inherit allarch localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-beta" install
    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
