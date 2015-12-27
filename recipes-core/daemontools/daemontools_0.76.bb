DESCRIPTION = "daemontools is a collection of tools for managing UNIX services."
HOMEPAGE = "http://cr.yp.to/daemontools.html"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/PD;md5=b3597d12946881e13cb3b548d1173851"

SECTION = "base"
PR = "r1"

INITSCRIPT_NAME = "svscanboot.sh"
INITSCRIPT_PARAMS = "start 30 5 2 ."

# not too pretty, but gets the job done, the bpp3 starts deamontools from startapp.sh
DO_UPDATE_RCD := "${@base_conditional('MACHINE', 'bpp3', 'base', 'update-rc.d', d)}"
inherit ${DO_UPDATE_RCD}

SRC_URI = " \
	http://cr.yp.to/daemontools/${PN}-${PV}.tar.gz \
	file://0001-support-cross-compiling.patch \
	file://0002-svscanboot-update-paths.patch \
	file://svscanboot.sh \
"
SRC_URI[md5sum] = "1871af2453d6e464034968a0fbcb2bfc"
SRC_URI[sha256sum] = "a55535012b2be7a52dcd9eccabb9a198b13be50d0384143bd3b32b8710df4c1f"

S = "${WORKDIR}/admin/daemontools-0.76"

do_compile() {
	./package/compile
}

do_install() {
	install -d ${D}${base_bindir}

	echo `ls ${S}/command`
	for cmd in `ls ${S}/command`; do
		install -m 0755 ${S}/command/$cmd ${D}${base_bindir}
	done

	mkdir -p ${D}/${sysconfdir}/init.d
	install -D ${WORKDIR}/svscanboot.sh ${D}/${sysconfdir}/init.d
}
