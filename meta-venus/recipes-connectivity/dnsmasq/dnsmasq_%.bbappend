FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

DEPENDS += "update-rc.d-native"

inherit localsettings

SRC_URI += "\
    file://0001-Don-t-spam-syslog-on-reload-and-not-using-hosts-file.patch \
    file://0001-Log-only-changes-to-nameserver-list.patch \
    file://dnsmasq.ap.conf \
    file://localsettings \
    file://resolv.conf \
    file://resolv-watch \
    file://rw-init \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"
hostap = ""
hostap:beaglebone = "1"
hostap:sunxi = "1"

RW_INITSCRIPT_PARAMS="start 80 5 . stop 10 0 1 6 ."

do_install:append() {
    install -m 644 ${UNPACKDIR}/resolv.conf ${D}${sysconfdir}

    install -d ${D}${sbindir}
    install -m 755 ${UNPACKDIR}/resolv-watch ${D}${sbindir}
    install -m 755 ${UNPACKDIR}/rw-init ${D}${sysconfdir}/init.d/resolv-watch
    update-rc.d -r ${D} resolv-watch ${RW_INITSCRIPT_PARAMS}

    if [ -n "${hostap}" ]; then
        install -m 644 ${UNPACKDIR}/dnsmasq.ap.conf ${D}${sysconfdir}
        ln -s dnsmasq ${D}${sysconfdir}/init.d/dnsmasq.ap
        update-rc.d -r ${D} dnsmasq.ap ${INITSCRIPT_PARAMS}
    fi
}
