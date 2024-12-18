SUMMARY = "A full modbus protocol written in python."
HOMEPAGE = "https://github.com/riptideio/pymodbus"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2c2223d66c7e674b40527b5a4c35bd76"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "git://github.com/riptideio/pymodbus.git;branch=master;protocol=https"
SRCREV = "fbdc470ae3e138c50e3659ec4ec8ebf39df58936"

S = "${WORKDIR}/git"

inherit setuptools3

RDEPENDS:${PN} = "python3-pyserial python3-six"
