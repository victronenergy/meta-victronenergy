FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://nanorc"

do_install:append () {
    mkdir -p ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/nanorc ${D}${sysconfdir}
}

