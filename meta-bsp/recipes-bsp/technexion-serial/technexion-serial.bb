LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
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

