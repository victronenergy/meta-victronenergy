FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://nanorc"

DEPENDS:remove = "file"

do_install:append () {
    mkdir -p ${D}${sysconfdir}
    install -m 0644 ${UNPACKDIR}/nanorc ${D}${sysconfdir}
}

