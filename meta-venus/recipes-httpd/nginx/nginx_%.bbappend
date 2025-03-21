FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

NGINX_USER = "www-data"
SYSTEMD_SERVICE:${PN} = ""
# only start nginx from init.d in testmode (no https, php etc)
INITSCRIPT_PARAMS = "start 99 4 . stop 20 0 1 6 ."
# Full nginx runs from daemontools
DAEMONTOOLS_RUN = "${sbindir}/start-nginx.sh"

SRC_URI += " \
    file://create-gui-redirect.sh \
    file://favicon.ico \
    file://default_server.site \
    file://localsettings \
    file://gui-v1.php \
    file://gui-v2.php \
    file://http.site \
    file://http-explanation.site \
    file://https.site \
    file://nginx-testmode.conf \
    file://start-nginx.sh \
"
PR = "3"

inherit daemontools localsettings www

RDEPENDS:${PN} += "php-fpm venus-www venus-www-config"
EXTRA_OECONF = "--error-log-path=/var/volatile/log/nginx/error.log"
PACKAGECONFIG:append = " http-auth-request"

do_install:append() {
    # don't package the logdir, create it as volatile storage instead
    rm -rf ${D}/var/volatile
    sed -i 's,/var/log/nginx,/var/volatile/log/nginx,g' ${D}${sysconfdir}/default/volatiles/99_nginx

cat - ${UNPACKDIR}/nginx.conf << EOF > ${D}${sysconfdir}/nginx/nginx.conf
daemon off;
EOF

    sed -i 's/^user.*/user ${NGINX_USER};/g' ${D}${sysconfdir}/nginx/nginx.conf
	sed -i 's,%SSL_CIPHERS,${OPENSSL_CIPHERSTRING_COLONS}:OPENSSL_CIPHERSUITES_COLONS,' ${D}${sysconfdir}/nginx/nginx.conf

    install -d ${D}${sbindir}
    install -m 755 ${UNPACKDIR}/start-nginx.sh ${D}${sbindir}

    install -m 644 ${UNPACKDIR}/nginx-testmode.conf ${D}${sysconfdir}/nginx
    echo 'DAEMON_OPTS="-c ${sysconfdir}/nginx/nginx-testmode.conf"' > "${D}${sysconfdir}/default/nginx"

    install -d ${D}${WWW_ROOT}
    install -m 644 ${UNPACKDIR}/favicon.ico ${D}${WWW_ROOT}
    install -m 644 ${UNPACKDIR}/gui-v1.php ${D}${WWW_ROOT}
    install -m 644 ${UNPACKDIR}/gui-v2.php ${D}${WWW_ROOT}
    ln -s /run/www/index.php ${D}${WWW_ROOT}

    install -d "${D}/etc/venus/www.d"
    install -m 755 "${UNPACKDIR}/create-gui-redirect.sh" "${D}/etc/venus/www.d"

    rm ${D}${sysconfdir}/nginx/sites-available/default_server
    rm ${D}${sysconfdir}/nginx/sites-enabled/default_server
    install -m 644 ${UNPACKDIR}/http.site ${D}${sysconfdir}/nginx/sites-available
    install -m 644 ${UNPACKDIR}/http-explanation.site ${D}${sysconfdir}/nginx/sites-available
    install -m 644 ${UNPACKDIR}/https.site ${D}${sysconfdir}/nginx/sites-available
}

