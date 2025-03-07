FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGECONFIG = ""

inherit useradd

USERADD_PACKAGES = "${PN}-fpm"
USERADD_PARAM:${PN}-fpm = "--system --no-create-home --shell /bin/false --user-group php-fpm"

do_install:append:class-target() {
    sed -e 's/^pm = dynamic$/pm = ondemand/g' -i "${D}${sysconfdir}/php-fpm.conf"
}
