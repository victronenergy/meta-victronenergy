FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://create-php-dirs"

PACKAGECONFIG:remove = "mysql imap"

inherit useradd

USERADD_PACKAGES = "${PN}-fpm"
USERADD_PARAM:${PN}-fpm = "--system --no-create-home --shell /bin/false --user-group php-fpm"

do_install:append() {
    install -d "${D}/etc/venus/www.d"
    install -m 755 "${WORKDIR}/create-php-dirs" "${D}/etc/venus/www.d"
}

FILES:${PN}-fpm += "${sysconfdir}/venus/www.d"
