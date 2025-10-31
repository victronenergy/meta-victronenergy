FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS += "readline"

INITSCRIPT_PARAMS = "start 70 5 2 3 4 . stop 22 0 1 6 ."

# do not adjust dirs, only import them
VELIB_DEFAULT_DIRS = "1"
inherit ve_package

# Always use the util-linux version, since it tries to write to
# /etc/adjtime which won't work with a read-only rootfs. The
# busybox version doesn't support --noadjfile though.
RDEPENDS:${PN} += "util-linux-hwclock"

SRC_URI += "\
    file://0001-Set-hwclock-when-time-is-decoded.patch \
    file://0002-Increase-NTP-poll-interval.patch \
    file://0003-gweb-Do-not-lookup-for-a-NULL-key-in-a-hash-table.patch \
    file://0004-wifi-make-max-connection-retries-configurable.patch \
    file://0005-service-Update-nameservers-and-timeservers-with-chan.patch \
    file://0006-main-add-new-AlwaysConnectedTechnologies-list-option.patch \
    file://0007-service-implement-AlwaysConnectedTechnologies-option.patch \
    file://0008-service-abstract-the-more-complex-autoconnect-condit.patch \
    file://0009-main.conf-document-AlwaysConnectedTechnologies-optio.patch \
    file://0010-make-ipv4ll-fallback-configurable.patch \
    file://0012-resolver-Fix-nameserver-and-search-domain-ordering-f.patch \
    file://0013-resolver-Don-t-export-domain-or-nameserver-duplicate.patch \
    file://0014-wifi-remove-load_shaping_retries-counter.patch \
    file://0015-service-attempt-auto-connect-of-services-in-failed-s.patch \
    file://0016-do-not-set-default-route-without-gateway.patch \
    file://0017-do-not-enable-rfkill-block-on-newly-discovered-devic.patch \
    file://0018-restart-daemon-on-wifi-failure.patch \
    file://0019-increase-metric-for-wifi-routes.patch \
    file://0001-disable-stat-files.patch \
    file://0021-silence-time-slew-log-spam.patch \
    file://0022-really-ignore-blacklisted-and-non-ethernet-interface.patch \
    file://0023-add-has_ipv-46-_gateway-helpers.patch \
    file://0024-preserve-default-gateway-when-new-link-lacks-one.patch \
    file://0025-do-not-add-0.0.0.0-as-nameserver.patch \
    file://0026-remove-special-route-setup-for-VPNs.patch \
    file://0027-technology-add-GatewayEnabled-property.patch \
    file://0028-service-use-gateway-only-if-permitted-for-technology.patch \
    file://0029-connection-check-if-gateway-is-permitted-for-service.patch \
    file://main.conf \
    file://connmand-watch.sh \
"

do_configure:append() {
    sed -i -e 's:\$(localstatedir)/lib:${permanentlocalstatedir}/lib:' ${B}/Makefile
}

do_install:append() {
    install -d ${D}${sysconfdir}/connman
    install -m 0644 ${UNPACKDIR}/main.conf ${D}${sysconfdir}/connman/main.conf
    install -m 0755 ${UNPACKDIR}/connmand-watch.sh ${D}${sbindir}/connmand-watch.sh
}
