FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGECONFIG:remove = "mysql imap"

inherit useradd

USERADD_PACKAGES = "${PN}-fpm"
USERADD_PARAM:${PN}-fpm = "--system --no-create-home --shell /bin/false --user-group php-fpm"
