DESCRIPTION = "miniUPnP Client"
SECTION = "networking"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c5ad90debd10be20aa95189a0a7c4253"
PR = "r0"
SRC_URI = "http://miniupnp.free.fr/files/download.php?file=miniupnpc-${PV}.tar.gz"


SRC_URI[md5sum] = "065bf20a20ebe605c675b7a5aaef340a"
SRC_URI[sha256sum] = "bc5f73c7b0056252c1888a80e6075787a1e1e9112b808f863a245483ff79859c"

CFLAGS += "-D_BSD_SOURCE -D_POSIX_C_SOURCE=1 -fPIC"

#EXTRA_OEMAKE = "INSTALLPREFIX=${D}/usr/"

do_install () {
    oe_runmake install INSTALLPREFIX=${D}/usr/
}
