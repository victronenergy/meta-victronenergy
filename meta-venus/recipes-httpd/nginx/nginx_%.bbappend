FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

NGINX_USER = "www-data"
SYSTEMD_SERVICE:${PN} = ""
# only start nginx from init.d in testmode (no https, php etc)
INITSCRIPT_PARAMS = "start 99 4 . stop 20 0 1 6 ."
# Full nginx runs from daemontools
DAEMONTOOLS_RUN = "${sbindir}/start-nginx.sh"

SRC_URI += " \
	file://default_server.site \
	file://nginx-testmode.conf \
	file://start-nginx.sh \
"
PR = "1"

inherit daemontools

RDEPENDS:${PN} += "php-fpm venus-www-config"
EXTRA_OECONF = "--error-log-path=/var/volatile/log/nginx/error.log"
PACKAGECONFIG:append = " http-auth-request"

do_install:append() {
    # don't package the logdir, create it as volatile storage instead
    rm -rf ${D}/var/volatile
    sed -i 's,/var/log/nginx,/var/volatile/log/nginx,g' ${D}${sysconfdir}/default/volatiles/99_nginx

cat - ${WORKDIR}/nginx.conf << EOF > ${D}${sysconfdir}/nginx/nginx.conf
daemon off;
EOF

    sed -i 's,/etc/,${sysconfdir}/,g' ${D}${sysconfdir}/nginx/nginx.conf
    sed -i 's,/var/,${localstatedir}/,g' ${D}${sysconfdir}/nginx/nginx.conf
    sed -i 's/^user.*/user ${NGINX_USER};/g' ${D}${sysconfdir}/nginx/nginx.conf
    sed -i 's,/var/log/nginx/,/var/volatile/log/nginx/,g' ${D}${sysconfdir}/nginx/nginx.conf
    sed -i '/vnd.wap.wmlc/a \ \ \ \ application/wasm                                 wasm;' ${D}${sysconfdir}/nginx/mime.types

    sed -i 's,\(gzip_types[^;]*\);,\1 application/wasm;,' ${D}${sysconfdir}/nginx/nginx.conf

    install -d ${D}${sbindir}
    install -m 755 ${WORKDIR}/start-nginx.sh ${D}${sbindir}

    install -m 644 ${WORKDIR}/nginx-testmode.conf ${D}${sysconfdir}/nginx
    echo 'DAEMON_OPTS="-c ${sysconfdir}/nginx/nginx-testmode.conf"' > "${D}${sysconfdir}/default/nginx"
}

