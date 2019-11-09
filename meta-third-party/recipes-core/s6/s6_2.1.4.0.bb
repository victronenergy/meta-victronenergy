DESCRIPTION = " \
    s6 is a small suite of programs for UNIX, designed to allow process supervision (a.k.a service supervision), \
    in the line of daemontools and runit, as well as various operations on processes and daemons. \
"

HOMEPAGE = "http://skarnet.org/software/s6/"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=1500f33d86c4956999052c0e137cd652"

SRC_URI = "http://skarnet.org/software/s6/s6-${PV}.tar.gz"
SRC_URI[md5sum] = "31bf3fa92568db3059c6765d553a74c3"
SRC_URI[sha256sum] = "e81853ecedb8280842de16e096c0a0170c5ceb8af7ce60824237588c9a374540"
PR = "r8"
S = "${WORKDIR}/${PN}-${PV}"
DEPENDS = "execline skalibs make-native"
RDEPENDS_${PN} = "execline skalibs"

do_configure() {
    ./configure --with-sysdeps=${PKG_CONFIG_SYSROOT_DIR}/usr/lib/skalibs/sysdeps --with-lib=${PKG_CONFIG_SYSROOT_DIR}/usr/lib --libexecdir=${libexecdir}
}

do_install () {
    oe_runmake 'DESTDIR=${D}' install
}
