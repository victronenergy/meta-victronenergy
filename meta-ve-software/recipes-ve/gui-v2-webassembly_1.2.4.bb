include gui-v2.inc

SRC_URI = " \
	https://github.com/victronenergy/gui-v2/releases/download/v${PV}/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}.zip \
	file://fix-makefile-to-copy-icons-to-the-correct-folder.patch \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "be7533e56d1bf44612884fd0cfb6d5570d05c7b1024ad9ae3fb6f3da882b1042"
S = "${UNPACKDIR}/wasm"

inherit allarch localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-v2" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
