include gui-v2.inc

SRC_URI = " \
	https://github.com/victronenergy/gui-v2/releases/download/v${PV}/venus-webassembly.zip;downloadfilename=venus-webassembly-${PV}.zip \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[sha256sum] = "882da093e9a908daea2c415d9fbc126bb0d4bc4b991946149e821ba23542cd89"

# Temporary: build the container off our own fork/tag instead of Victron's
# official release, so the firmware-update navigation hides itself when
# swupdate isn't present (see venus-platform_2.49.bb for the D-Bus side of
# this). Drop this override once these changes land in an official gui-v2
# release - at that point the container just uses the same SRC_URI/
# sha256sum above as everyone else.
SRC_URI:venus-container = " \
	https://github.com/nmbath/gui-v2/releases/download/v1.3.22-OCI/venus-webassembly.zip;downloadfilename=venus-webassembly-v1.3.22-OCI.zip;name=venuscontaineroci \
	file://calc-gui-v2-wasm-sha26.sh \
	file://localsettings \
"
SRC_URI[venuscontaineroci.sha256sum] = "03c50d283a3158f3bc5446308baa58d500cfa2c9a233c2eff8ff040980f4c593"

S = "${UNPACKDIR}/wasm"

inherit localsettings www

do_install() {
    make DESTDIR="${D}" PREFIX="${WWW_ROOT}/gui-v2" install
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/calc-gui-v2-wasm-sha26.sh ${D}${bindir}
}

RDEPENDS:${PN} += "bash"
