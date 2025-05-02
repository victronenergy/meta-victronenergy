SUMMARY = "A full modbus protocol written in python."
HOMEPAGE = "https://github.com/riptideio/pymodbus"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2c2223d66c7e674b40527b5a4c35bd76"

SRC_URI = "git://github.com/riptideio/pymodbus.git;branch=master;protocol=https"
SRCREV = "c5772b35ae3f29d1947f3ab453d8d00df846459f"

SRC_URI += "\
    file://0001-validate-transaction-id-of-response.patch \
"

S = "${WORKDIR}/git"

inherit setuptools3

DEPENDS = "python3-six-native python3-setuptools-native"
RDEPENDS:${PN} = "python3-pyserial python3-six"
