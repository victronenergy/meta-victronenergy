DESCRIPTION = "Check if a filesystem has been modified"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "e2fsprogs"

SRC_URI = "file://fsmodified.c"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} ${UNPACKDIR}/${PN}.c -o ${PN} -lext2fs
}

do_install() {
    install -d ${D}${sbindir}
    install -m 755 ${PN} ${D}${sbindir}
}
