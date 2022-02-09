SUMMARY = "command to react when a partition becomes ro"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://is-ro-partition.c"

do_compile () {
    ${CC} ${CFLAGS} ${LDFLAGS} ${WORKDIR}/is-ro-partition.c -o is-ro-partition
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${B}/is-ro-partition ${D}${bindir}
}

