FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit www

SRC_URI += " \
    file://hiawatha.conf \
    file://0001-disable-client-challenge.patch \
"
RDEPENDS_${PN} += "php-fpm"

EXTRA_OECMAKE = " -DENABLE_IPV6=OFF \
                  -DENABLE_CACHE=OFF \
                  -DENABLE_DEBUG=OFF \
                  -DENABLE_TLS=OFF \
                  -DENABLE_TOOLKIT=ON \
                  -DENABLE_CHROOT=OFF \
                  -DENABLE_XSLT=ON \
                  -DENABLE_TOMAHAWK=OFF \
                  -DCMAKE_INSTALL_MANDIR=${mandir} \
                  -DCMAKE_INSTALL_BINDIR=${bindir} \
                  -DCMAKE_INSTALL_SBINDIR=${sbindir} \
                  -DCMAKE_INSTALL_SYSCONFDIR=${sysconfdir} \
                  -DCMAKE_INSTALL_LIBDIR=${libdir} \
                  -DLOG_DIR=/var/log/hiawatha \
                  -DPID_DIR=/var/run \
                  -DWEBROOT_DIR=/var/www/hiawatha \
                  -DWORK_DIR=/var/lib/hiawatha "

do_configure_append() {
    sed -i 's:^WebsiteRoot\>.*:WebsiteRoot = ${WWW_ROOT}:' \
        ${WORKDIR}/hiawatha.conf
}

do_install_append() {
    install -m 0644 ${WORKDIR}/hiawatha.conf ${D}${sysconfdir}/hiawatha/hiawatha.conf
    install -d ${D}${sysconfdir}/hiawatha/sites-enabled
    install -d ${D}${sysconfdir}/default/volatiles
    echo "d root root 0755 ${localstatedir}/volatile/log/hiawatha none" \
         > ${D}${sysconfdir}/default/volatiles/99_hiawatha
    rm -rf ${D}${localstatedir}/log
}
