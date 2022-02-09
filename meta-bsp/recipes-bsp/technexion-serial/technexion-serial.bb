LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
S = "${WORKDIR}"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += "\
    file://main.c \
"

do_compile () {
    ${CC} ${CFLAGS} ${LDFLAGS} ${WORKDIR}/main.c -o technexion-serial
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${B}/technexion-serial ${D}${bindir}
}

