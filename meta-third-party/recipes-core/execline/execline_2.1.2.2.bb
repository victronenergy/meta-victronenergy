DESCRIPTION = "execline is a (non-interactive) scripting language, like sh ; but its syntax is quite different from a traditional shell syntax"
HOMEPAGE = "http://skarnet.org/software/skalibs"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=1500f33d86c4956999052c0e137cd652"

SRC_URI = "http://skarnet.org/software/execline/execline-2.1.2.2.tar.gz"
SRC_URI[md5sum] = "851e253a8aca47814af2eca581a12c22"
SRC_URI[sha256sum] = "aea08c2c1b986c91f300d40737dd43067b91705d2c729859344ec65f989aec06"
PR = "r4"

S = "${WORKDIR}/${PN}-${PV}"
DEPENDS = "skalibs make-native"
RDEPENDS_${PN} = "skalibs"

do_configure() {
    ./configure --with-sysdeps=${PKG_CONFIG_SYSROOT_DIR}/usr/lib/skalibs/sysdeps --libdir=${libdir} --dynlibdir=${libdir} --with-lib=${PKG_CONFIG_SYSROOT_DIR}/usr/lib
}

do_install () {
    oe_runmake 'DESTDIR=${D}' install
}
