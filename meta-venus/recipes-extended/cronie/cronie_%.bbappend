FILESEXTRAPATHS:prepend := "${THISDIR}/cronie:"

SRC_URI += "file://crontab"

do_install:append () {
    install -m 0755 ${UNPACKDIR}/crontab ${D}${sysconfdir}
    chmod 600 ${D}${sysconfdir}/crontab
}

