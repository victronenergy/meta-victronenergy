FILESEXTRAPATHS:prepend := "${THISDIR}/sysvinit-inittab:"

SRC_URI += "file://autologin"

do_install:append () {
    install -d ${D}/${base_sbindir}
    install -m 0755 ${UNPACKDIR}/autologin ${D}/${base_sbindir}

    sed -i -e 's:/bin/start_getty:/sbin/getty -L:' ${D}${sysconfdir}/inittab
}

FILES:${PN} += "${base_sbindir}"
