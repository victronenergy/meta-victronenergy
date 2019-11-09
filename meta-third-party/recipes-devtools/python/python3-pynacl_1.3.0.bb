DESCRIPTION = "Python binding to the Networking and Cryptography (NaCl) library"
HOMEPAGE = "https://github.com/pyca/pynacl"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8cc789b082b3d97e1ccc5261f8594d3f"

DEPENDS = "libsodium-native python3-cffi-native libsodium"

PYPI_PACKAGE = "PyNaCl"

SRC_URI[md5sum] = "3f3b1272287bc2de8efd8a5fa18bf728"
SRC_URI[sha256sum] = "0c6100edd16fefd1557da078c7a31e7b7d7a52ce39fdca2bec29d4f7b6e7600c"

inherit setuptools3 pypi

do_compile_prepend () {
    sed -i -e 's:configure, "--dis:configure, "--host=${TARGET_SYS}", "--dis:g' \
    -e 's:.*make.*check.*:#&:g' \
    ${S}/setup.py
}

export SODIUM_INSTALL="system"
