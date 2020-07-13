SUMMARY = "A Python library to encode/decode DNS wire-format packets"
HOMEPAGE = "https://github.com/paulc/dnslib"
SECTION = "devel/python"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8d24d8757f793508dc751f2037c49333"

SRC_URI = "git://github.com/paulc/dnslib.git;protocol=http;tag=${PV}"
S = "${WORKDIR}/git"

inherit setuptools

RDEPENDS_${PN} = "python-pyserial python-six"
