# NOTE: the recipe comes form meta-homeassistant, but they only support OE master.
# https://github.com/meta-homeassistant/meta-homeassistant/commit/99df21ba008269d5802c3c1077972bc6e637742f
#
# Bluetooth support is dropped, not used in Venus.

SUMMARY = "Python library to control Shelly"
HOMEPAGE = "https://github.com/home-assistant-libs/aioshelly"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=dab31a1d28183826937f4b152143a33f"

inherit pypi python_setuptools_build_meta ptest

SRC_URI[sha256sum] = "024bd9a04d2bcfd13c444fe7b17798b532c8fbe1b8793b56395ecdb3fdf2890c"

RDEPENDS:${PN} = " \
    python3-core \
    python3-aiohttp \
    python3-core \
    python3-orjson \
"

