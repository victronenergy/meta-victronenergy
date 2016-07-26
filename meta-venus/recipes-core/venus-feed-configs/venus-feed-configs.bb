DESCRIPTION = "Venus pkg feeds (for development)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit deploy

addtask deploy before do_populate_sysroot after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"

DISTRO_FEED_DIR = "venus/${COREVERSION}/develop"
DISTRO_FEED_URI ?= "https://updates.victronenergy.com/feeds/${DISTRO_FEED_DIR}"
DISTRO_FEED_ARCHS ?= "all ${PACKAGE_EXTRA_ARCHS} ${MACHINE_ARCH}"

do_compile() {
	echo -n > ${S}/opkg.conf
	for feed in ${DISTRO_FEED_ARCHS}; do
		echo "src/gz ${feed} ${DISTRO_FEED_URI}/${feed}" >> ${S}/opkg.conf
	done
}

do_install () {
	install -d ${D}${sysconfdir}/opkg
	install -m 0644 ${S}/opkg.conf ${D}${sysconfdir}/opkg/venus.conf
}

do_deploy () {
	echo ${DISTRO_FEED_DIR} > ${DEPLOY_DIR}/upload-ipk
}

CONFFILES_${PN} += "${sysconfdir}/opkg/venus.conf"

