SUMMARY = "Bounded local data-exchange service for Venus OS"
DESCRIPTION = "D-Bus controlled, nginx-fronted exchange broker for bounded local transfers"
HOMEPAGE = "https://github.com/nmbath/venus-exchange"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e32777d272d59cf1a64aed9ee0ad1fc1"

SRC_URI = " \
    gitsm://github.com/nmbath/venus-exchange.git;branch=main;protocol=ssh;user=git \
"
SRCREV = "341cbeb2711345758a328f31ca640eaebfd84c98"
S = "${WORKDIR}/git"

inherit allarch daemontools python-compile useradd ve_package

DAEMONTOOLS_RUN = "${vedir}/${PN}/start-venus-exchange"

GROUPADD_PARAM:${PN} = "-g 976 venus-exchange"
USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--no-create-home --home-dir /nonexistent --system --shell /bin/false -u 976 -g venus-exchange venus-exchange"

RDEPENDS:${PN} = " \
    nginx \
    python3-core \
    python3-dbus \
    python3-pygobject \
    venus-web-pages \
"

# The legacy compatibility service must not be supervised or installed
# alongside the canonical exchange service.
RCONFLICTS:${PN} = "venus-import"
RREPLACES:${PN} = "venus-import"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    oe_runmake -C ${S} \
        DESTDIR=${D} \
        prefix=${vedir}/${PN} \
        INSTALL_COMPAT=0 \
        install

    install -m 0755 ${S}/packaging/venus/run \
        ${D}${vedir}/${PN}/start-venus-exchange

    install -d ${D}${sysconfdir}/nginx/conf.d
    install -m 0644 ${S}/packaging/venus/nginx-http.conf \
        ${D}${sysconfdir}/nginx/conf.d/venus-exchange-http.conf
    install -d ${D}${sysconfdir}/nginx/locations-enabled
    install -m 0644 ${S}/packaging/venus/nginx-location.conf \
        ${D}${sysconfdir}/nginx/locations-enabled/venus-exchange.conf
}

FILES:${PN} += " \
    ${sysconfdir}/nginx/conf.d/venus-exchange-http.conf \
    ${sysconfdir}/nginx/locations-enabled/venus-exchange.conf \
"

pkg_preinst:${PN}() {
    if [ -z "$D" ] && [ -d /service/venus-import ]; then
        svc -d /service/venus-import || true
        [ ! -d /service/venus-import/log ] || svc -d /service/venus-import/log || true
    fi
}

pkg_postinst:${PN}() {
    if [ -z "$D" ]; then
        if [ -L /run/venus-import ]; then
            rm -f -- /run/venus-import
        elif [ -d /run/venus-import ]; then
            rm -rf -- /run/venus-import
        fi

        if [ -L /service/venus-import ]; then
            rm -f -- /service/venus-import
        elif [ -d /service/venus-import ]; then
            rm -rf -- /service/venus-import
        fi
        if [ -d /opt/victronenergy/service/venus-import ]; then
            rm -rf -- /opt/victronenergy/service/venus-import
        fi

        for site in \
            /etc/nginx/sites-available/http.site \
            /etc/nginx/sites-available/https.site
        do
            if [ -f "$site" ]; then
                sed -i '\|include /etc/nginx/venus-import-location.conf;|d' "$site"
            fi
        done
        rm -f -- /etc/nginx/conf.d/venus-import-http.conf
        rm -f -- /etc/nginx/venus-import-location.conf

        nginx -t || exit 1
        [ ! -d /service/nginx ] || svc -t /service/nginx
    fi
}

pkg_postrm:${PN}() {
    if [ -z "$D" ]; then
        case "$1" in
            upgrade|failed-upgrade)
                exit 0
                ;;
        esac

        # Preserve the replay ledger in /data/venus-exchange. Remove only
        # runtime state and explicitly disposable staging areas.
        rm -rf -- /run/venus-exchange
        rm -rf -- /var/volatile/venus-exchange
        rm -rf -- /data/volatile/venus-exchange

        nginx -t || exit 1
        [ ! -d /service/nginx ] || svc -t /service/nginx
    fi
}
