DESCRIPTION = "Requests: HTTP for Humans"
HOMEPAGE = "http://docs.python-requests.org/en/latest/"
SECTION = "devel/python"
LICENSE = "Apache-2.0"

RDEPENDS_${PN} = "python python-netserver python-email"
PR = "r0"

SRC_URI = "https://github.com/kennethreitz/requests/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "432811320b4af6028277cf1e64c86ec6"
SRC_URI[sha256sum] = "f12a828a1c4845cc5d23bb86ff36d04636b06eb393366c19a3e7f080d1d0601a"

LIC_FILES_CHKSUM = "file://LICENSE;md5=c7869e52c8275537186de35e3cd5f9ec"
S = "${WORKDIR}/requests-${PV}/"

inherit setuptools

# need to export these variables for python-config to work
export BUILD_SYS
export HOST_SYS
export STAGING_INCDIR
export STAGING_LIBDIR

BBCLASSEXTEND = "native"

do_install_append() {
  rm -f ${D}${libdir}/python*/site-packages/site.py*
}
