SUMMARY = "command to react when a partition becomes ro"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://is-ro-partition.c"

do_compile () {
    ${CC} ${CFLAGS} ${LDFLAGS} ${WORKDIR}/is-ro-partition.c -o is-ro-partition
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${B}/is-ro-partition ${D}${bindir}
}

