DESCRIPTION = "SSH2 protocol library"
HOMEPAGE = "https://github.com/paramiko/paramiko/"
SECTION = "devel/python"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fd0120fc2e9f841c73ac707a30389af5"

SRC_URI[md5sum] = "f729a45c9122bf975b9177ec5aaa3791"
SRC_URI[sha256sum] = "f4b2edfa0d226b70bd4ca31ea7e389325990283da23465d572ed1f70a7583041"

inherit pypi

RDEPENDS_${PN} += "\
    ${PYTHON_PN}-pycrypto \
    ${PYTHON_PN}-bcrypt \
    ${PYTHON_PN}-cryptography \
    ${PYTHON_PN}-pynacl \
    ${PYTHON_PN}-unixadmin \
"
