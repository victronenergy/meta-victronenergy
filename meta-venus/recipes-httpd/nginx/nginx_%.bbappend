FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

NGINX_USER = "www-data"
SYSTEMD_SERVICE:${PN} = ""
INITSCRIPT_PARAMS = "disable"
DAEMONTOOLS_RUN = "${sbindir}/start-nginx.sh"

SRC_URI += " \
	file://default_server.site \
	file://start-nginx.sh \
"

inherit daemontools

RDEPENDS:${PN} += "php-fpm venus-www-config"

do_install:append() {
	sed -i 's,/var/log/nginx,/var/volatile/log/nginx,g' ${D}${sysconfdir}/default/volatiles/99_nginx

cat - ${WORKDIR}/nginx.conf << EOF > ${D}${sysconfdir}/nginx/nginx.conf
daemon off;
EOF

    sed -i 's,/etc/,${sysconfdir}/,g' ${D}${sysconfdir}/nginx/nginx.conf
    sed -i 's,/var/,${localstatedir}/,g' ${D}${sysconfdir}/nginx/nginx.conf
    sed -i 's/^user.*/user ${NGINX_USER};/g' ${D}${sysconfdir}/nginx/nginx.conf
    sed -i 's,/var/log/nginx/,/var/volatile/log/nginx/,g' ${D}${sysconfdir}/nginx/nginx.conf

    install -d ${D}${sbindir}
    install -m 755 ${WORKDIR}/start-nginx.sh ${D}${sbindir}
}

