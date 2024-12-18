SUMMARY = "A Python library to encode/decode DNS wire-format packets"
HOMEPAGE = "https://github.com/paulc/dnslib"
SECTION = "devel/python"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8d24d8757f793508dc751f2037c49333"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\S+)"
SRC_URI = "git://github.com/paulc/dnslib.git;branch=master;protocol=https"
SRCREV = "8d33ec952f3913881cfe2a972b866cb546798a7b"
S = "${WORKDIR}/git"

inherit setuptools3
