DESCRIPTION = "Venus pkg feeds (for development)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"
DISTRO_FEED_ARCHS = "all ${TUNE_PKGARCH} ${MACHINE_ARCH}"
FEEDS = "develop testing candidate release"

do_compile() {
    for feed in ${FEEDS}; do
        echo -n > ${S}/opkg-$feed.conf
        for arch in ${DISTRO_FEED_ARCHS}; do
            echo "src/gz ${arch} https://updates.victronenergy.com/feeds/venus/${feed}/packages/${COREVERSION}/${arch}" >> ${S}/opkg-$feed.conf
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


