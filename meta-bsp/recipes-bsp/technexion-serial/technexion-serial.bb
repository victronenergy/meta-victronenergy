LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += "\
    file://main.c \
"

do_compile () {
    ${CC} ${CFLAGS} ${LDFLAGS} ${UNPACKDIR}/main.c -o technexion-serial
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${B}/technexion-serial ${D}${bindir}
}

