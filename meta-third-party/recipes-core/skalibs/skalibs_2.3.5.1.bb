DESCRIPTION = "skalib"
HOMEPAGE = "http://skarnet.org/software/skalibs"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=1500f33d86c4956999052c0e137cd652"

SRC_URI = " \
    http://skarnet.org/software/skalibs/skalibs-2.3.5.1.tar.gz \
    file://sysdeps.cfg \
"

SRC_URI[md5sum] = "d18e9361194e478f685a05b814658c0f"
SRC_URI[sha256sum] = "9ef75d87bf6d586e828888e4cee6adbb74c06d6afcdb50f6c395644ce0e561d4"
PR = "r4"
S = "${WORKDIR}/${PN}-${PV}"

do_configure() {
    ./configure --with-sysdeps=${WORKDIR}/sysdeps.cfg --libdir=${libdir} --dynlibdir=${libdir}
}

do_install () {
    oe_runmake 'DESTDIR=${D}' install
}
