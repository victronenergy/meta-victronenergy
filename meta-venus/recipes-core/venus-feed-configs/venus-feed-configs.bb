DESCRIPTION = "Venus pkg feeds (for development)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DISTRO_FEED_DIR = "venus/packages/${COREVERSION}/develop"
DISTRO_FEED_URI ?= "https://updates.victronenergy.com/feeds/${DISTRO_FEED_DIR}"
DISTRO_FEED_ARCHS = "all ${DEFAULTTUNE} ${MACHINE_ARCH}"
FEEDS = "develop testing candidate release"

PR = "r1"

do_compile() {
	for feed in ${FEEDS}; do
		echo -n > ${S}/opkg-$feed.conf
		for arch in ${DISTRO_FEED_ARCHS}; do
			echo "src/gz ${arch} ${DISTRO_FEED_URI}/${arch}" >> ${S}/opkg-$feed.conf
		done
	done
}

do_install () {
	install -d ${D}${datadir}/${PN}
	for feed in ${FEEDS}; do
		install -m 0644 ${S}/opkg-$feed.conf ${D}${datadir}/${PN}
	done

	install -d ${D}${sysconfdir}/opkg
	ln -s ${datadir}/${PN}/opkg-release.conf ${D}${sysconfdir}/opkg/venus.conf
}


