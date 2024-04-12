SUMMARY = "A zero-dependency DBus library for Python with asyncio support"
HOMEPAGE = "https://github.com/acrisci/python-dbus-next"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://PKG-INFO;md5=48efab8ed4f9b8e97a9bd87e27b458b8"

SRC_URI[md5sum] = "0e31605bd90f3460aebcd0bb7fe0dc20"
SRC_URI[sha256sum] = "f4eae26909332ada528c0a3549dda8d4f088f9b365153952a408e28023a626a5"

PYPI_PACKAGE = "dbus_next"

inherit pypi setuptools3

BBCLASSEXTEND = "native nativesdk"
