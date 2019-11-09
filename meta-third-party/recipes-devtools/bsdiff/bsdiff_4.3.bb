DESCRIPTION = "daemontools is a collection of tools for managing UNIX services."
HOMEPAGE = "http://www.daemonology.net/bsdiff/"

PR = "0"

DEPENDS = "bzip2"
RDEPENDS_${PN} = "bzip2"

SRC_URI = "http://www.daemonology.net/bsdiff/bsdiff-4.3.tar.gz \
       file://bsdiff-4.3/Makefile"

LICENSE = "BSD-2-Clause"
SRC_URI[md5sum] = "e6d812394f0e0ecc8d5df255aa1db22a"
SRC_URI[sha256sum] = "18821588b2dc5bf159aa37d3bcb7b885d85ffd1e19f23a0c57a58723fea85f48"

LIC_FILES_CHKSUM = "file://bsdiff.c;endline=26;md5=7352f2c7111e9b6ddb2699505b96feca"

S = "${WORKDIR}/${PN}-${PV}"

do_configure () {
}

#EXTRA_OEMAKE = "all"

do_install () {
    install -d ${D}/${bindir}
    install -m 0755 ${S}/bsdiff ${D}/${bindir}
    install -m 0755 ${S}/bspatch ${D}/${bindir}
}
