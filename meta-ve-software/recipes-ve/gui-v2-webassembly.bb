include gui-v2.inc

SRC_URI = " \
	https://github.com/victronenergy/gui-v2/releases/download/v${PV}/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}.zip \
	file://localsettings \
	file://victron-energy-os-license-v1.txt \
    file://0001-make-the-wasm-version-work-over-https.patch \
"
SRC_URI[sha256sum] = "${WASM_SHA256SUM}"
S = "${WORKDIR}/wasm"
LIC_FILES_CHKSUM = "file://${WORKDIR}/victron-energy-os-license-v1.txt;md5=2bfa2d9036ce1bc186352ff84c84a1bb"

inherit allarch localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-beta" install
}

